/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
            sessionProvider = new SessionProvider(whenHibernateIsInitialisedOn(givenAPluginInstance()));
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
