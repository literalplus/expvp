/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.spawn;

import java.util.List;
import java.util.Optional;

/**
 * Provides access to spawns and keeps track of the currently selected one.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-12
 */
public interface SpawnService {
    Optional<MapSpawn> getCurrentSpawn();

    void forceNextSpawn(MapSpawn spawn);

    /**
     * @return all the spawns known to this service that have a location set
     */
    List<MapSpawn> getSpawns();

    Optional<MapSpawn> getSpawnById(String spawnId);

    void saveSpawn(MapSpawn spawn);
}
