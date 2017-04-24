/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.model.friend;

import me.minotopia.expvp.api.friend.FriendRequest;
import me.minotopia.expvp.api.model.PlayerData;

import java.util.Collection;
import java.util.Optional;

/**
 * Provides access to friend requests.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
public interface FriendRequestRepository {
    Collection<FriendRequest> findReceivedRequests(PlayerData data);

    Optional<FriendRequest> findSentRequest(PlayerData data);

    FriendRequest create(PlayerData source, PlayerData target);

    void delete(FriendRequest request);
}
