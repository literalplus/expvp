/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
