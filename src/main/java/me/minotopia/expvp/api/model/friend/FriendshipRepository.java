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

package me.minotopia.expvp.api.model.friend;

import li.l1t.common.exception.DatabaseException;
import me.minotopia.expvp.api.friend.Friendship;
import me.minotopia.expvp.api.model.PlayerData;

import java.util.Optional;

/**
 * Provides friendship instances from the backend.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
public interface FriendshipRepository {
    /**
     * Finds the single friendship containing given player.
     *
     * @param playerData the player to find
     * @return an optional containing the found friendship, or an empty optional if there is no such friendship
     * @throws DatabaseException if a database error occurs or a database inconsistency is detected
     */
    Optional<Friendship> findFriendshipWith(PlayerData playerData) throws DatabaseException;

    /**
     * @param friendship the friendship to delete
     * @throws IllegalArgumentException if given friendship wasn't fetched from this repository
     */
    void delete(Friendship friendship);

    Friendship create(PlayerData source, PlayerData target);
}
