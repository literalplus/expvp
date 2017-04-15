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
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public YamlSpawnService(SpawnManager spawnManager, PlayerInitService initService, SpawnVoteTask spawnVoteTask) {
        this.spawnManager = spawnManager;
        initService.registerInitHandler(this::teleportToSpawnIfPossible);
        spawnVoteTask.start();
    }

    @Override
    public Optional<MapSpawn> getCurrentSpawn() {
        if (currentSpawn == null) {
            findRandomSpawn().ifPresent(this::forceNextSpawn);
        }
        return Optional.ofNullable(currentSpawn);
    }

    private Optional<MapSpawn> findRandomSpawn() {
        List<MapSpawn> spawns = getSpawns();
        if (spawns.isEmpty()) {
            return Optional.empty();
        } else {
            int randomIndex = RandomUtils.nextInt(0, spawns.size());
            return Optional.of(spawns.get(randomIndex));
        }
    }

    @Override
    public void forceNextSpawn(MapSpawn spawn) {
        currentSpawn = spawn;
    }

    @Override
    public List<MapSpawn> getSpawns() {
        return spawnManager.getAll().stream()
                .filter(MapSpawn::hasLocation)
                .collect(Collectors.toList());
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

    @Override
    public void teleportToSpawnIfPossible(Player player) {
        Optional<MapSpawn> currentSpawn = getCurrentSpawn();
        if (currentSpawn.isPresent()) {
            player.teleport(currentSpawn.get().getLocation());
        } else {
            I18n.sendLoc(player, Format.warning("core!respawn.no-spawn"));
        }
    }
}
