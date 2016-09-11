/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;

import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import me.minotopia.expvp.player.HibernatePlayerDataService;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HibernateInitTest extends HibernateAwareTest {
    @Test
    public void testInitHibernate() throws IOException {
        //given
        EPPlugin plugin = givenAPluginInstance();
        //when
        whenHibernateIsInitialisedOn(plugin);
        //then no exception should be thrown
    }

    @Test
    public void testPlayerDataService() throws IOException {
        //given
        HibernatePlayerDataService service = givenAPlayerDataService();
        HibernatePlayerData someData = service.findOrCreateDataMutable(UUID.randomUUID());
        service.saveData(someData);
        //then
        assertThat(service.findOrCreateData(someData.getUniqueId()), is(someData));
    }

    private HibernatePlayerDataService givenAPlayerDataService() throws IOException {
        return new HibernatePlayerDataService(givenHibernateIsInitialised());
    }

}
