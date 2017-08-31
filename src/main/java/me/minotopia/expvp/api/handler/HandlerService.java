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

package me.minotopia.expvp.api.handler;

import me.minotopia.expvp.api.model.PlayerData;

import java.util.stream.Stream;

/**
 * Manages the specific handlers for players' skill sets. Dynamically enables handlers when they are requested and
 * disables them when there is no player that needs them.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public interface HandlerService {
    /**
     * Registers all handlers required by given player's skill set and notes that given player requires these skills.
     *
     * @param playerData the player to operate on
     */
    void registerHandlers(PlayerData playerData);

    /**
     * Unregisters handlers required by given player's skill set that are not required by any other player's skill set.
     *
     * @param playerData the player to operate on
     */
    void unregisterHandlers(PlayerData playerData);

    /**
     * Creates a stream of handlers that are instances of given type and included in given player's skill set.
     *
     * @param type       the type of handler to request
     * @param playerData the player whose skill set to check with
     * @param <T>        the type of handler that is returned
     * @return a stream of handlers that are instances of type and in given player's skill set
     */
    <T extends SkillHandler> Stream<T> handlersOfTypeStream(Class<? extends T> type, PlayerData playerData);
}
