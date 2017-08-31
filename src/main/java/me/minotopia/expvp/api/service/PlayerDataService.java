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

package me.minotopia.expvp.api.service;

import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * A service that manages player data instances.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-08
 */
public interface PlayerDataService {
    /**
     * Finds existing data for a player in the underlying data store. If no data exists yet, creates
     * a new data set for the player. <p><b>Note:</b> The data returned by this method is read-only.
     * Use {@link #findOrCreateDataMutable(UUID)} for write access.</p>
     * <p><b>Note:</b> For the create action to be persisted, any surrounding scoped sessions needs to be committed.</p>
     *
     * @param playerId the unique id of the player
     * @return the existing or created player data
     */
    PlayerData findOrCreateData(UUID playerId);

    /**
     * Finds existing data for a player in the underlying data store. If no data exists yet, creates
     * a new data set for the player. <p><b>Note:</b> Changes to the returned object are in memory
     * only. Use {@link #saveData(MutablePlayerData)} to persist changes to the data store.</p>
     *
     * @param playerId the unique id of the player
     * @return the existing or created player data
     */
    MutablePlayerData findOrCreateDataMutable(UUID playerId);

    Optional<? extends PlayerData> findData(UUID playerId);

    Optional<? extends MutablePlayerData> findDataMutable(UUID playerId);

    /**
     * Consumes an {@link #findOrCreateDataMutable(UUID) existing or new} data object for given player.
     *
     * @param playerId the unique id of the player whose data to consume
     * @param what     the action to invoke on the data
     */
    void withMutable(UUID playerId, Consumer<MutablePlayerData> what);

    /**
     * Persists the state of a given player data object to the underlying data store.
     *
     * @param toSave the player data to save
     */
    void saveData(MutablePlayerData toSave);
}
