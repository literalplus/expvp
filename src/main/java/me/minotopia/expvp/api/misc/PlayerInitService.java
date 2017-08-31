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

package me.minotopia.expvp.api.misc;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Manages registration of initialisation (e.g. join) and deinitialisation (e.g. leave) tasks for players.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-05
 */
public interface PlayerInitService {
    /**
     * Calls all registered init handlers.
     *
     * @param player the player to (re-)initialise
     */
    void callInitHandlers(Player player);

    /**
     * Registers an initialisation handler.
     *
     * @param handler the handler to register
     */
    void registerInitHandler(Consumer<Player> handler);

    /**
     * Calls all registered de-init handlers.
     *
     * @param player the player to deinitialise
     */
    void callDeInitHandlers(Player player);

    /**
     * Registers a deinitialisation handler.
     *
     * @param handler the handler to register
     */
    void registerDeInitHandler(Consumer<Player> handler);
}
