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

package me.minotopia.expvp.api.friend;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

/**
 * Manages friend requests.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
public interface FriendRequestService {
    /**
     * @param player the player whose requests to query
     * @return the collection of still-valid friend requests this player has received
     */
    Collection<FriendRequest> findReceivedRequests(Player player);

    /**
     * @param player the player whose request to query
     * @return an optional containing the single still-valid friend request given player has sent, or an empty optional
     * if there is no such request
     */
    Optional<FriendRequest> findSentRequest(Player player);

    FriendRequest requestFriendship(Player source, Player target);

    void cancelRequest(Player source);

    Friendship acceptRequest(FriendRequest request);
}
