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

package me.minotopia.expvp.model.player;

import me.minotopia.expvp.HibernateAwareTest;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import me.minotopia.expvp.util.ScopedSession;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
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
