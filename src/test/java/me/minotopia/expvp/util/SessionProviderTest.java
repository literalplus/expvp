/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.util;

import li.l1t.common.intake.exception.InternalException;
import me.minotopia.expvp.HibernateAwareTest;
import org.hibernate.HibernateException;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class SessionProviderTest extends HibernateAwareTest {
    @Test
    public void testSingleScope() throws IOException {
        //given
        SessionProvider provider = givenASessionProvider();
        //when
        ScopedSession scopedOuter = provider.scoped();
        try (ScopedSession scoped = scopedOuter) {
            scoped.join().tx();
            scoped.commitIfLast();
        } catch (Exception e) {
            throw scopedOuter.handleException(e);
        }
        //then
        assertThat(scopedOuter.getReferenceCount(), is(0));
        assertThat(scopedOuter.acceptsFurtherReferences(), is(false));
    }

    private SessionProvider givenASessionProvider() throws IOException {
        return givenHibernateIsInitialised();
    }

    @Test
    public void testNestedScope() throws IOException {
        //given
        SessionProvider provider = givenASessionProvider();
        //when
        ScopedSession scopedOuter = provider.scoped();
        try (ScopedSession scoped = scopedOuter) {
            scoped.join().tx();
            try (ScopedSession scoped2 = provider.scoped()) {
                scoped2.join().tx();
                assertThat(scoped2.getReferenceCount(), is(2));
                assertThat(scoped2, is(scopedOuter));
                scoped2.commitIfLast();
            }
            assertThat(scoped.tx().isActive(), is(true));
            scoped.commitIfLast();
        } catch (Exception e) {
            throw scopedOuter.handleException(e);
        }
        //then
        assertThat(scopedOuter.getReferenceCount(), is(0));
        assertThat(scopedOuter.acceptsFurtherReferences(), is(false));
    }

    @Test
    public void testNestedRollbackNoException() throws IOException {
        //given
        SessionProvider provider = givenASessionProvider();
        //when
        ScopedSession scopedOuter = provider.scoped();
        try (ScopedSession scoped = scopedOuter) {
            scoped.join().tx();
            try (ScopedSession scoped2 = provider.scoped()) {
                scoped2.join().tx().rollback();
            }
            assertThat(scoped.tx().isActive(), is(false));
            scoped.commitIfLast(); //throws a HibernateException
        } catch (HibernateException e) {
            assertThat(scopedOuter.acceptsFurtherReferences(), is(false));
            assertThat(scopedOuter.getReferenceCount(), is(0));
            return;
        }
        //this should never happen
        throw new AssertionError("commitIfLast() did not throw after rollback");
    }

    @Test
    public void testNestedRollbackWithException() throws IOException {
        //given
        SessionProvider provider = givenASessionProvider();
        //when
        ScopedSession scopedOuter = provider.scoped();
        try (ScopedSession scoped = scopedOuter) {
            scoped.join().tx();
            tryScopedRollbackAndThrow(provider);
            throw new AssertionError("tryScopedRollbackAndThrow() did not throw");
        } catch (InternalException e) {
            assertThat(scopedOuter.acceptsFurtherReferences(), is(false));
            assertThat(scopedOuter.getReferenceCount(), is(0));
        }
    }

    private void tryScopedRollbackAndThrow(SessionProvider provider) {
        try (ScopedSession scoped2 = provider.scoped()) {
            scoped2.join().tx().rollback();
            throw new InternalException("just propagating a rollback");
        }
    }
}
