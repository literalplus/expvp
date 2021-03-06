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

package me.minotopia.expvp.api.handler.kit;

import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Manages compiled kits, caching compilations and forwarding to a compiler when necessary. Provides
 * invalidation methods for other subsystems to hook into and invalidate the compiled kit, for
 * example if the collection of skills known to a player changes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-18
 */
public interface CompiledKitCache {
    /**
     * Gets a compiled kit for a player, either by retrieving it from the cache or forwarding the
     * call to an appropriate compiler.
     *
     * @param player     the player to obtain a kit for
     * @param playerData the player data corresponding to given player
     * @return a compiled kit for given player
     */
    KitCompilation getKitFor(Player player, PlayerData playerData);

    /**
     * Invalidates the cached kit for a single player.
     *
     * @param playerId the unique id of the player
     */
    void invalidateKit(UUID playerId);
}
