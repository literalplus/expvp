/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.util;

import li.l1t.common.exception.InternalException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import javax.persistence21.RollbackException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides ScopedSession instances scoped to the current thread. Recreates sessions after they have
 * been closed.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-11
 */
public class SessionProvider {
    private final SessionFactory sessionFactory;
    private ThreadLocal<ScopedSession> sessionLocal = new ThreadLocal<>();
    private ReentrantLock sessionLock = new ReentrantLock();

    public SessionProvider(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Gets the current session used by the current thread if a session scope already exists or
     * crates a new session and scope otherwise.
     *
     * @return a scoped session
     * @throws InternalException if the session lock cannot be obtained
     */
    public ScopedSession scoped() {
        attemptLockSessionLock();
        try {
            ScopedSession currentSession = sessionLocal.get();
            if (!isValidSession(currentSession)) {
                currentSession = new ScopedSession(sessionFactory);
                sessionLocal.set(currentSession);
            }
            return currentSession;
        } finally {
            sessionLock.unlock();
        }
    }

    private boolean isValidSession(ScopedSession currentSession) {
        return currentSession != null && currentSession.acceptsFurtherReferences();
    }

    private void attemptLockSessionLock() {
        try {
            if (!sessionLock.tryLock(2, TimeUnit.SECONDS)) {
                throw new InternalException("Failed to obtain lock for getting a session in 2 seconds");
            }
        } catch (InterruptedException e) {
            throw new InternalException("Interrupted while waiting for session lock", e);
        }
    }

    /**
     * Wraps given exception with an appropriate error message as internal error for catching in
     * higher application layers. If a valid session exists in the current thread, initiates a
     * rollback and closes it. The called is expected to throw the returned exception.
     *
     * @param e the exception to wrap
     * @return the internal exception wrapping the parameter exception
     */
    public InternalException handleException(Exception e) {
        closeAndRollbackIfDirty(sessionLocal.get());
        if (e instanceof RollbackException || e instanceof javax.persistence.RollbackException) {
            return new InternalException("Datenbankfehler (Rollback)", e);
        } else if (e instanceof HibernateException) {
            return new InternalException("Datenbankfehler", e);
        } else {
            return new InternalException("Fehler während der Interaktion mit der Datenbank", e);
        }
    }

    private void closeAndRollbackIfDirty(ScopedSession scoped) {
        if (isValidSession(scoped)) {
            if (scoped.hasTransaction()) {
                scoped.rollbackAndClose();
            } else {
                scoped.forceClose();
            }
        }
    }

    /**
     * Gets the session factory used by this provider. Note that direct creation of sessions is not
     * supported and may lead to unexpected behaviour. Always prefer {@link #scoped()} for session
     * access.
     *
     * @return the underlying session factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
