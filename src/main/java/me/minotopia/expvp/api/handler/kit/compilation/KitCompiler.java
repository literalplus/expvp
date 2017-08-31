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

package me.minotopia.expvp.api.handler.kit.compilation;

import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.entity.Player;

/**
 * Compiles the appropriate kit for a player by resolving the list of kit items from handlers of
 * applicable skills into a list of item stacks.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
public interface KitCompiler {
    /**
     * Compiles a kit template for a specific player.
     *
     * @param player the player to compile for
     * @param data   the player data of given player
     * @return a template with the compiled item stacks
     */
    KitCompilation compile(Player player, PlayerData data);
}
