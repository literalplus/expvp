/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.minotopia.expvp.util;

import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An auto-closeable session instance that keeps the count of scopes currently using it. Intended
 * for use with try...finally statements. <p><b>Important:</b> Before accessing any state of this
 * object, join() must be called. After the current unit of work (usually a try block) is done,
 * close() must be called. If this is not adhered, the reference count will become invalid and the
 * session might not get closed properly.</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-11
 */
public class ScopedSession implements AutoCloseable {
    private static final Logger LOGGER = LoggingManager.getLogger(ScopedSession.class);
    private final AtomicInteger refCount = new AtomicInteger(0);
    private Session session;
    private Transaction transaction = null;

    ScopedSession(SessionFactory factory) {
        this.session = factory.openSession();
    }

    /**
     * Joins this scoped session and returns itself. Note that a joined session needs to be released
     * using {@link #close()} to make sure it will get properly closed.
     *
     * @return the scoped session that was just joined
     */
    public ScopedSession join() {
        refCount.incrementAndGet();
        return this;
    }

    public Session session() {
        checkHasSession();
        return session;
    }

    private void checkHasSession() {
        if (session == null) {
            throw new HibernateException("No session associated with this scope!");
        }
    }

    /**
     * @return the transaction currently associated with this scoped session or a new one if none
     * exists yet
     */
    public Transaction tx() {
        if (transaction == null) {
            transaction = session.beginTransaction();
        }
        return transaction;
    }

    public void commit() {
        checkHasTransaction();
        transaction.commit();
    }

    private void checkHasTransaction() {
        if (transaction == null) {
            throw new HibernateException("Cannot set active state if no transaction is associated with this scope!");
        } else if (!transaction.isActive()) {
            throw new HibernateException("Transaction is no longer active!");
        }
    }

    public void rollbackAndClose() {
        checkHasTransaction();
        transaction.rollback();
        forceClose();
    }

    /**
     * Commits the underlying transaction if this layer is the last layer.
     */
    public void commitIfLast() {
        if (refCount.get() <= 1) {
            commit();
        }
    }

    /**
     * Commits the underlying transaction if this layer is the last layer and a transaction was
     * crated in a lower layer.
     */
    public void commitIfLastAndChanged() {
        if (transaction != null) {
            commitIfLast();
        }
    }


    @Override
    public void close() throws HibernateException {
        if (refCount.decrementAndGet() <= 0) {
            forceClose();
        }
    }

    void forceClose() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
            closeInternal();
            throw new HibernateException("Transaction was not completed cleanly in last ref count! Rolled back forcefully.");
        }
        closeInternal();
    }

    private void closeInternal() {
        refCount.set(0);
        session.close();
        session = null;
        transaction = null;
    }

    /**
     * @return whether this scoped session is currently being referenced by a scope
     */
    public boolean hasReferences() {
        return refCount.get() != 0;
    }

    /**
     * @return whether this session is able to accept further references by a scope
     */
    public boolean acceptsFurtherReferences() {
        return session != null; //this happens if we're closed
    }

    int getReferenceCount() {
        return refCount.get();
    }

    public boolean hasTransaction() {
        return transaction != null && transaction.isActive();
    }
}
