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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Manages PlayerData instances using a Hibernate backend.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-14
 */
@Singleton
public class HibernatePlayerDataService implements PlayerDataService {
    private final SessionProvider sessionProvider;

    @Inject
    public HibernatePlayerDataService(SessionProvider sessionProvider) {
        this.sessionProvider = Preconditions.checkNotNull(sessionProvider, "sessionProvider");
    }

    @Override
    public HibernatePlayerData findOrCreateData(UUID playerId) {
        return findOrCreateDataMutable(playerId); //read-only is a type safety thing only, this simplifies usage
    }

    @Override
    public HibernatePlayerData findOrCreateDataMutable(UUID playerId) {
        Preconditions.checkNotNull(playerId, "playerId");
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            Optional<HibernatePlayerData> optional =
                    scoped.session().byId(HibernatePlayerData.class).loadOptional(playerId);
            if (optional.isPresent()) {
                return optional.get();
            }

            scoped.tx();
            HibernatePlayerData playerData = new HibernatePlayerData(playerId);
            scoped.session().save(playerData);
            scoped.commitIfLast();
            return playerData;
        }
    }

    @Override
    public Optional<HibernatePlayerData> findData(UUID playerId) {
        return findDataMutable(playerId); //read-only is a type safety thing only, this simplifies usage
    }

    @Override
    public Optional<HibernatePlayerData> findDataMutable(UUID playerId) {
        Preconditions.checkNotNull(playerId, "playerId");
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            return scoped.session().byId(HibernatePlayerData.class).loadOptional(playerId);
        }
    }

    @Override
    public void withMutable(UUID playerId, Consumer<MutablePlayerData> what) {
        Preconditions.checkNotNull(playerId, "playerId");
        sessionProvider.inSession(ignored -> {
            HibernatePlayerData data = findOrCreateDataMutable(playerId);
            what.accept(data);
            saveData(data);
        });
    }

    @Override
    public void saveData(MutablePlayerData toSave) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            scoped.tx();
            scoped.session().saveOrUpdate(toSave);
            scoped.commitIfLast();
        }
    }
}
