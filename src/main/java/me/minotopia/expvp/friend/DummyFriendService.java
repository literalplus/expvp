/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.friend;

import me.minotopia.expvp.api.friend.FriendService;
import me.minotopia.expvp.api.model.PlayerData;

import java.util.Optional;

/**
 * Nobody has friends in this amazing service
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
public class DummyFriendService implements FriendService {
    @Override
    public Optional<PlayerData> findFriend(PlayerData data) {
        return Optional.empty();
    }
}
