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

import li.l1t.common.exception.InternalException;
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
            throw provider.handleException(e);
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
            throw provider.handleException(e);
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
