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

package me.minotopia.expvp.friend;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import me.minotopia.expvp.api.friend.FriendService;
import me.minotopia.expvp.api.friend.Friendship;
import me.minotopia.expvp.api.friend.exception.NoFriendshipException;
import me.minotopia.expvp.api.misc.PlayerService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.model.friend.FriendshipRepository;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
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
                        .map(findOtherFriend(player.getUniqueId()))
        );
    }

    private Function<Friendship, PlayerData> findOtherFriend(UUID ownId) {
        return friendship -> {
            if (ownId.equals(friendship.getSource().getUniqueId())) {
                return friendship.getTarget();
            } else {
                return friendship.getSource();
            }
        };
    }

    @Override
    public Optional<PlayerData> findFriend(PlayerData data) {
        Preconditions.checkNotNull(data, "data");
        return sessionProvider.inSessionAnd(ignored ->
                players.findData(data.getUniqueId())
                        .flatMap(friendshipRepository::findFriendshipWith)
                        .map(findOtherFriend(data.getUniqueId()))
        );
    }

    @Override
    public void removeFriend(Player player) {
        sessionProvider.inSession(ignored -> {
                    Optional<Friendship> friendship = players.findData(player.getUniqueId())
                            .flatMap(friendshipRepository::findFriendshipWith);
                    if (friendship.isPresent()) {
                        deleteFriendship(friendship.get());
                    } else {
                        throw new NoFriendshipException();
                    }
                }
        );
    }

    private void deleteFriendship(Friendship friendship) {
        friendshipRepository.delete(friendship);
        notifyFriendshipEnded(friendship.getTarget());
        notifyFriendshipEnded(friendship.getSource());
    }

    private void notifyFriendshipEnded(PlayerData data) {
        playerService.findOnlinePlayer(data.getUniqueId())
                .ifPresent(player -> I18n.sendLoc(player, Format.broadcast("core!friend.fs-ended")));
    }
}
