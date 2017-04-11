/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.respawn;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Handles special effects on players respawning.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-06
 */
public interface RespawnService {
    /**
     * Starts the pre-respawn process for given player, delaying their actual respawn by a small implementation-specific
     * amount of time. Assumes that the player is still at their death location. This will start the actual respawn
     * process once the delay has elapsed.
     *
     * @param player the player to start the delay for
     */
    void startRespawnDelay(Player player);

    /**
     * Starts the actual respawn process of given player. This includes teleporting them to the current map's spawn and
     * opening the research menu if they have any Talent Points left to spend. If given player has no TP left, this
     * method immediately calls {@link #startPostRespawn(Player)}. Otherwise, that method is called once research menu
     * is closed.
     *
     * @param player the player to respawn
     */
    void startRespawn(Player player);

    /**
     * Starts the post-respawn tasks for given player. This includes rich notifications for league changes since the
     * previous respawn.
     *
     * @param player the player to respawn
     */
    void startPostRespawn(Player player);

    /**
     * Queues a league change for fancy display after given player has respawned.
     *
     * @param playerId the unique id of the player to notify upon respawn
     */
    void queueLeagueChange(UUID playerId);
}
