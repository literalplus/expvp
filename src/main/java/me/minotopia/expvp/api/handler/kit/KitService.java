/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Service that manages compilation and applying of the appropriate kit for a player according to
 * their set of skills.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-18
 */
public interface KitService {
    /**
     * Applies a kit to given player that corresponds to their set of skills.
     *
     * @param player the player
     */
    void applyKit(Player player);

    /**
     * Invalidates any cache this service is aware of relating to given player.
     *
     * @param playerId the unique id of the player
     */
    void invalidateCache(UUID playerId);

    //TODO: If this is the main entry point from outside the module, we need methods for unregistering handlers or some kind of skill delete listener
}
