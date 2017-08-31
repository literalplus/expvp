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

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Service that manages compilation and applying of the appropriate kit for a player according to
 * their set of skills.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
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
}
