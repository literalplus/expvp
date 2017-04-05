/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.util;

import li.l1t.common.exception.InternalException;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.i18n.exception.I18nInternalException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import javax.persistence21.RollbackException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Provides ScopedSession instances scoped to the current thread. Recreates sessions after they have
 * been closed.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-11
 */
public class SessionProvider {
    private final SessionFactory sessionFactory;
    private final EPPlugin plugin;
    private ThreadLocal<ScopedSession> sessionLocal = new ThreadLocal<>();
    private ReentrantLock sessionLock = new ReentrantLock();

    public SessionProvider(SessionFactory sessionFactory, EPPlugin plugin) {
        this.sessionFactory = sessionFactory;
        this.plugin = plugin;
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
            return new I18nInternalException("error!db.hibernate-rollback", e);
        } else if (e instanceof HibernateException) {
            return new I18nInternalException("error!db.hibernate-misc", e);
        } else {
            return new I18nInternalException("error!db.other", e);
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

    /**
     * Executes some code in a scoped session and {@link ScopedSession#commitIfLastAndChanged() commits afterwards, if
     * necessary}.
     *
     * @param what what to do in the session
     */
    public void inSession(Consumer<ScopedSession> what) {
        try (ScopedSession scoped = scoped().join()) {
            what.accept(scoped);
            scoped.commitIfLastAndChanged();
        }
    }

    /**
     * Executes some code in a scoped session {@link ScopedSession#commitIfLastAndChanged() commits afterwards if
     * necessary}, and returns some value.
     *
     * @param what what to do in the session
     * @return the result of calling given function
     */
    public <T> T inSessionAnd(Function<ScopedSession, T> what) {
        T result;
        try (ScopedSession scoped = scoped().join()) {
            result = what.apply(scoped);
            scoped.commitIfLastAndChanged();
        }
        return result;
    }

    /**
     * Executes some code in a scoped session in {@link EPPlugin#async(Runnable) an async thread} and {@link
     * ScopedSession#commitIfLastAndChanged() commits afterwards, if necessary}.
     *
     * @param what what to do in the session
     */
    public void inSessionAsync(Consumer<ScopedSession> what) {
        plugin.async(() -> {
            inSession(what);
        });
    }

    /**
     * Executes some code in a scoped session in {@link EPPlugin#async(Runnable) an async thread} {@link
     * ScopedSession#commitIfLastAndChanged() commits afterwards if necessary}, and returns some value.
     *
     * @param what what to do in the session
     * @return the result of calling given function
     */
    public <T> CompletableFuture<T> inSessionAsync(Function<ScopedSession, T> what) {
        CompletableFuture<T> future = new CompletableFuture<>();
        plugin.async(() -> {
            try {
                future.complete(inSessionAnd(what));
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
