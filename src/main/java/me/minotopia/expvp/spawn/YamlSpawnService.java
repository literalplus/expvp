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

package me.minotopia.expvp.spawn;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.exception.InternalException;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.GameMode;
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
@ConstructOnEnable
@Singleton
public class YamlSpawnService implements SpawnService {
    private MapSpawn currentSpawn;
    private final SpawnManager spawnManager;

    @Inject
    public YamlSpawnService(SpawnManager spawnManager, PlayerInitService initService) {
        this.spawnManager = spawnManager;
        initService.registerInitHandler(this::teleportToSpawnIfPossible);
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
        if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.CREATIVE) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}
