/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;

import li.l1t.common.test.util.MockHelper;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.hibernate.SessionFactory;

import java.io.File;
import java.io.IOException;

/**
 * Abstract base class for tests that need access to a Hibernate SessionFactory.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-11
 */
public abstract class HibernateAwareTest {
    protected SessionFactory whenHibernateIsInitialisedOn(EPPlugin plugin) throws IOException {
        plugin.initHibernate(Thread.currentThread().getContextClassLoader());
        return plugin.getSessionFactory();
    }

    protected SessionFactory givenHibernateIsInitialised() throws IOException {
        return whenHibernateIsInitialisedOn(givenAPluginInstance());
    }

    @SuppressWarnings("deprecation")
    protected EPPlugin givenAPluginInstance() {
        return new EPPlugin(
                new JavaPluginLoader(MockHelper.mockServer()),
                new PluginDescriptionFile("Expvp", "awesome", EPPlugin.class.getName()),
                new File("target/"), new File("./thisshouldbeignored--unittest")
        );
    }
}
