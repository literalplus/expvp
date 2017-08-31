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

package me.minotopia.expvp.api.respawn;

import org.bukkit.entity.Player;

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
}
