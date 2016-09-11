/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.player;

import me.minotopia.expvp.HibernateAwareTest;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import me.minotopia.expvp.util.ScopedSession;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class HibernatePlayerDataServiceTest extends HibernateAwareTest {
    @Test
    public void testPlayerDataService() throws IOException {
        //given
        HibernatePlayerDataService service = givenAPlayerDataService();
        //when
        HibernatePlayerData someData = whenSomeDataIsCreatedAndSavedIn(service, UUID.randomUUID());
        //then
        assertThat(service.findOrCreateData(someData.getUniqueId()), is(someData));
    }

    private HibernatePlayerData whenSomeDataIsCreatedAndSavedIn(HibernatePlayerDataService service, UUID playerId) {
        HibernatePlayerData someData = service.findOrCreateDataMutable(playerId);
        service.saveData(someData);
        return someData;
    }

    private HibernatePlayerDataService givenAPlayerDataService() throws IOException {
        return new HibernatePlayerDataService(givenHibernateIsInitialised());
    }

    @Test
    public void testPlayerDataServiceScoped() throws IOException {
        //given
        HibernatePlayerDataService service = givenAPlayerDataService();
        //when
        HibernatePlayerData someData;
        try (ScopedSession scoped = getOrCreateSessionProvider().scoped().join()) {
            UUID playerId = UUID.randomUUID();
            someData = whenSomeDataIsCreatedAndSavedIn(service, playerId);
            assertThat(scoped.session().isDirty(), is(true));
            assertThat(service.findOrCreateData(playerId), is(sameInstance(someData)));
            scoped.commitIfLast();
        }
        //then
        PlayerData outsideData = service.findOrCreateData(someData.getUniqueId());
        assertThat(outsideData, is(someData));
        assertThat(outsideData, is(not(sameInstance(someData))));
    }
}
