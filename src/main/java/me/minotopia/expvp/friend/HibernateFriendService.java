/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.friend;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import me.minotopia.expvp.api.friend.FriendService;
import me.minotopia.expvp.api.friend.Friendship;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.model.friend.HibernateFriendshipRepository;

import java.util.Optional;
import java.util.function.Function;

/**
 * A friend service that resolves friendships using Hibernate.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
public class HibernateFriendService implements FriendService {
    private final HibernateFriendshipRepository friendshipRepository;

    @Inject
    public HibernateFriendService(HibernateFriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public Optional<PlayerData> findFriend(PlayerData data) {
        Preconditions.checkNotNull(data, "data");
        return friendshipRepository.findFriendshipWith(data)
                .map(findOtherFriend(data));
    }

    private Function<Friendship, PlayerData> findOtherFriend(PlayerData self) {
        return friendship -> {
            if (self.equals(friendship.getSource())) {
                return friendship.getTarget();
            } else {
                return friendship.getSource();
            }
        };
    }

    @Override
    public void removeFriend(PlayerData data) {
        friendshipRepository.findFriendshipWith(data)
                .ifPresent(friendshipRepository::delete);
    }
}
