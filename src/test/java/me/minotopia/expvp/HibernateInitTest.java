/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;

import li.l1t.common.test.util.MockHelper;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import me.minotopia.expvp.player.HibernatePlayerDataService;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-08
 */
public class HibernateInitTest {
    @Test
    public void testInitHibernate() throws IOException {
        //given
        EPPlugin plugin = createPlugin();
        //when
        plugin.initHibernate(Thread.currentThread().getContextClassLoader());
        HibernatePlayerDataService service = new HibernatePlayerDataService(plugin.getSessionFactory());
        HibernatePlayerData someData = service.findOrCreateDataMutable(UUID.randomUUID());
        service.saveData(someData);
        //then
        assertThat(service.findOrCreateData(someData.getUniqueId()), is(someData));
        //having more than one test method apparently involves way more complexity (and time)
    }

    @SuppressWarnings("deprecation")
    private EPPlugin createPlugin() {
        return new EPPlugin(
                new JavaPluginLoader(MockHelper.mockServer()),
                new PluginDescriptionFile("Expvp", "awesome", EPPlugin.class.getName()),
                new File("target/"), new File("./thisshouldbeignored--unittest")
        );
    }
}
