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

package me.minotopia.expvp;

import me.minotopia.expvp.util.SessionProvider;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;

import java.io.IOException;

/**
 * Abstract base class for tests that need access to a Hibernate SessionFactory.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-11
 */
public abstract class HibernateAwareTest extends EPPluginAwareTest {
    private static SessionFactory sessionFactory;
    private SessionProvider sessionProvider;

    @AfterClass
    public static void shutdownHibernate() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    protected SessionFactory whenHibernateIsInitialisedOn(EPPlugin plugin) throws IOException {
        if (sessionFactory == null) {
            sessionFactory = plugin.initHibernate(Thread.currentThread().getContextClassLoader());
        }
        return sessionFactory;
    }

    protected SessionProvider givenHibernateIsInitialised() throws IOException {
        if (sessionProvider == null) {
            sessionProvider = new SessionProvider(whenHibernateIsInitialisedOn(givenAPluginInstance()), plugin);
        }
        return sessionProvider;
    }

    protected SessionProvider getOrCreateSessionProvider() throws IOException {
        if (sessionProvider == null) {
            givenHibernateIsInitialised();
        }
        return sessionProvider;
    }

}
