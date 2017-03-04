/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;

import li.l1t.common.test.util.MockHelper;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.junit.Before;

import java.io.File;

/**
 * Abstract base class for unit tests that need access to a EPPlugin instance.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public class EPPluginAwareTest {
    protected EPPlugin plugin;

    @Before
    @SuppressWarnings("deprecation")
    public void createEPPluginInstance() {
        plugin = new EPPlugin(
                new JavaPluginLoader(MockHelper.mockServer()),
                new PluginDescriptionFile("Expvp", "awesome", EPPlugin.class.getName()),
                new File("target/"), new File("./thisshouldbeignored--unittest")
        );
    }

    protected EPPlugin givenAPluginInstance() {
        return plugin;
    }
}
