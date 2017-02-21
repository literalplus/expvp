/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.service;

import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;

import java.util.UUID;

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

    /**
     * Persists the state of a given player data object to the underlying data store.
     *
     * @param toSave the player data to save
     */
    void saveData(MutablePlayerData toSave);
}
