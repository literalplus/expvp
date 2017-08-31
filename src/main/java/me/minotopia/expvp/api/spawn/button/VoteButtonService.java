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
