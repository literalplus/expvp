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
import me.minotopia.expvp.api.misc.PlayerService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.model.friend.FriendshipRepository;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.function.Function;

/**
 * A friend service that resolves friendships using Hibernate.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
public class HibernateFriendService implements FriendService {
    private final FriendshipRepository friendshipRepository;
    private final PlayerDataService players;
    private final SessionProvider sessionProvider;
    private final PlayerService playerService;

    @Inject
    public HibernateFriendService(FriendshipRepository friendshipRepository, PlayerDataService players,
                                  SessionProvider sessionProvider, PlayerService playerService) {
        this.friendshipRepository = friendshipRepository;
        this.players = players;
        this.sessionProvider = sessionProvider;
        this.playerService = playerService;
    }

    @Override
    public Optional<PlayerData> findFriend(Player player) {
        Preconditions.checkNotNull(player, "player");
        return sessionProvider.inSessionAnd(ignored ->
                players.findData(player.getUniqueId())
                        .flatMap(friendshipRepository::findFriendshipWith)
                        .map(findOtherFriend(player))
        );
    }

    private Function<Friendship, PlayerData> findOtherFriend(Player self) {
        return friendship -> {
            if (self.getUniqueId().equals(friendship.getSource().getUniqueId())) {
                return friendship.getTarget();
            } else {
                return friendship.getSource();
            }
        };
    }

    @Override
    public void removeFriend(Player player) {
        sessionProvider.inSession(ignored ->
                players.findData(player.getUniqueId())
                        .flatMap(friendshipRepository::findFriendshipWith)
                        .ifPresent(this::deleteFriendship)
        );
    }

    private void deleteFriendship(Friendship friendship) {
        friendshipRepository.delete(friendship);
        notifyFriendshipEnded(friendship.getTarget());
        notifyFriendshipEnded(friendship.getSource());
    }

    private void notifyFriendshipEnded(PlayerData data) {
        playerService.findOnlinePlayer(data.getUniqueId())
                .ifPresent(player -> I18n.sendLoc(player, "core!friend.fs-ended"));
    }
}
