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

package me.minotopia.expvp.api.spawn;

import org.bukkit.entity.Player;

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

    /**
     * Teleports given player to the current spawn, if possible. This might not be possible due to no spawns existing or
     * due to no spawns with a location existing. If the teleport fails, an informational message is sent to given
     * player.
     *
     * @param player the player to teleport
     */
    void teleportToSpawnIfPossible(Player player);
}
