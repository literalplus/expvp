/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.spawn.button;

import me.minotopia.expvp.api.spawn.MapSpawn;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Provides access to vote button data. <p><b>Note:</b> All locations passed to this service should be block-level
 * locations, i.e. not have pitch or yaw set.</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-19
 */
public interface VoteButtonService {
    void setButton(Location location, MapSpawn spawn);

    void removeButton(Location location);

    Optional<VoteButton> getButtonAt(Location location);

    /**
     * Starts a linking session for given spawn. That means that the next button given player interacts with will be a
     * vote button for given spawn.
     *
     * @param player the player to initiate the session for
     * @param spawn  the spawn to link, or null to unlink
     */
    void startLinkingSession(Player player, MapSpawn spawn);

    /**
     * Gets and removes the current linking session for given player.
     *
     * @param player the player to query the current linking session for
     * @return an optional containing the spawn given player is currently linking, or an empty optional if given player
     * has not started a linking session
     */
    Optional<MapSpawn> getLinkingSession(Player player);
}
