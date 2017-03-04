/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
