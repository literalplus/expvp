/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.spawn;

import com.google.inject.Inject;
import li.l1t.common.exception.InternalException;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnService;
import org.apache.commons.lang3.RandomUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Keeps track of the current spawn and provides an API for accessing spawns.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-12
 */
public class YamlSpawnService implements SpawnService {
    private MapSpawn currentSpawn;
    private final SpawnManager spawnManager;

    @Inject
    public YamlSpawnService(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }

    @Override
    public Optional<MapSpawn> getCurrentSpawn() {
        if (currentSpawn == null && !spawnManager.getAll().isEmpty()) {
            List<MapSpawn> spawns = new ArrayList<>(spawnManager.getAll());
            int randomIndex = RandomUtils.nextInt(0, spawns.size());
            MapSpawn randomSpawn = spawns.get(randomIndex);
            forceNextSpawn(randomSpawn);
        }
        return Optional.ofNullable(currentSpawn);
    }

    @Override
    public void forceNextSpawn(MapSpawn spawn) {
        currentSpawn = spawn;
    }

    @Override
    public Optional<MapSpawn> getSpawnById(String spawnId) {
        return Optional.ofNullable(spawnManager.get(spawnId));
    }

    @Override
    public void saveSpawn(MapSpawn spawn) {
        try {
            spawnManager.save(spawn);
        } catch (IOException e) {
            throw new InternalException("Unable to save spawn " + spawn, e);
        }
    }
}
