/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.util;

import li.l1t.common.intake.exception.InternalException;
import org.hibernate.SessionFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides ScopedSession instances scoped to the current thread. Recreates sessions after they have
 * been closed.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
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
            if (currentSession == null || !currentSession.acceptsFurtherReferences()) {
                currentSession = new ScopedSession(sessionFactory);
                sessionLocal.set(currentSession);
            }
            return currentSession;
        } finally {
            sessionLock.unlock();
        }
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
