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
     * Starts the pre-respawn process for given player. Assumes that the player is already at the correct spawn
     * location. This sets the player into bed state until the respawn delay has passed.
     *
     * @param player the player to respawn
     */
    void startPreRespawn(Player player);

    /**
     * @param player the player to check
     * @return whether given player's respawn delay has passed yet
     */
    boolean hasDelayPassed(Player player);

    /**
     * Starts the actual respawn process of given player. This includes opening the research menu if they have any
     * Talent Points left to spend. If there are no actual respawn tasks to do (e.g. if the player has no TP left),
     * this method immediately calls {@link #startPostRespawn(Player)}. Otherwise, that method is called once the
     * respawn tasks are finished.
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
