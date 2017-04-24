/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.friend;

import me.minotopia.expvp.api.model.PlayerData;

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
     * @param data the player whose requests to query
     * @return the collection of still-valid friend requests this player has received
     */
    Collection<FriendRequest> findReceivedRequests(PlayerData data);

    /**
     * @param data the player whose request to query
     * @return an optional containing the single still-valid friend request given player has sent, or an empty optional
     * if there is no such request
     */
    Optional<FriendRequest> findSentRequest(PlayerData data);

    FriendRequest requestFriendship(PlayerData source, PlayerData target);

    void cancelRequest(PlayerData source);

    Friendship acceptRequest(FriendRequest request);
}
