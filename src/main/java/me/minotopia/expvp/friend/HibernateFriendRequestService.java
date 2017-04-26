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
import li.l1t.common.chat.ComponentSender;
import li.l1t.common.chat.XyComponentBuilder;
import me.minotopia.expvp.api.friend.FriendRequest;
import me.minotopia.expvp.api.friend.FriendRequestService;
import me.minotopia.expvp.api.friend.Friendship;
import me.minotopia.expvp.api.friend.exception.ExistingFriendshipException;
import me.minotopia.expvp.api.friend.exception.NoRequestException;
import me.minotopia.expvp.api.friend.exception.PendingRequestException;
import me.minotopia.expvp.api.friend.exception.SelfFriendException;
import me.minotopia.expvp.api.misc.PlayerService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.model.friend.FriendRequestRepository;
import me.minotopia.expvp.api.model.friend.FriendshipRepository;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.util.SessionProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Accesses friend requests using Hibernate. Also sends notifications.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
public class HibernateFriendRequestService implements FriendRequestService {
    private final FriendRequestRepository requestRepository;
    private final FriendshipRepository friendshipRepository;
    private final PlayerService playerService;
    private final PlayerDataService players;
    private final SessionProvider sessionProvider;

    @Inject
    public HibernateFriendRequestService(FriendRequestRepository requestRepository, FriendshipRepository friendshipRepository,
                                         PlayerService playerService, PlayerDataService players, SessionProvider sessionProvider) {
        this.requestRepository = requestRepository;
        this.friendshipRepository = friendshipRepository;
        this.playerService = playerService;
        this.players = players;
        this.sessionProvider = sessionProvider;
    }

    @Override
    public Collection<FriendRequest> findReceivedRequests(Player player) {
        return sessionProvider.inSessionAnd(ignored ->
                players.findData(player.getUniqueId())
                        .map(requestRepository::findReceivedRequests)
                        .orElse(Collections.emptyList())
        );
    }

    @Override
    public Optional<FriendRequest> findSentRequest(Player player) {
        return sessionProvider.inSessionAnd(ignored ->
                players.findData(player.getUniqueId())
                        .flatMap(requestRepository::findSentRequest)
        );
    }

    @Override
    public FriendRequest requestFriendship(Player source, Player target) {
        Preconditions.checkNotNull(source, "source");
        Preconditions.checkNotNull(target, "target");
        return sessionProvider.inSessionAnd(ignored -> {
            PlayerData sourceData = players.findOrCreateData(source.getUniqueId());
            PlayerData targetData = players.findOrCreateData(target.getUniqueId());
            checkSelfFriend(source, target);
            checkExistingFriendship(sourceData);
            checkPendingRequest(source);
            FriendRequest request = requestRepository.create(sourceData, targetData);
            notifyIncomingRequest(target, source);
            return request;
        });
    }

    private void notifyIncomingRequest(Player targetPlayer, Player source) {
        ComponentSender.sendTo(
                targetPlayer,
                TextComponent.fromLegacyText(I18n.loc(targetPlayer, Format.broadcast("core!friend.req-inc", source.getName()))),
                new XyComponentBuilder(" ").color(ChatColor.GREEN)
                        .append(I18n.loc(targetPlayer, "core!friend.req-inc-btn"))
                        .hintedCommand("/fs accept " + source.getUniqueId())
                        .create()
        );
    }

    private void checkSelfFriend(Player source, Player target) {
        if (source.equals(target)) {
            throw new SelfFriendException();
        }
    }

    private void checkExistingFriendship(PlayerData source) {
        Optional<Friendship> existingFriendship = friendshipRepository.findFriendshipWith(source);
        if (existingFriendship.isPresent()) {
            throw new ExistingFriendshipException();
        }
    }

    private void checkPendingRequest(Player source) {
        Optional<FriendRequest> pendingRequest = findSentRequest(source);
        if (pendingRequest.isPresent()) {
            throw new PendingRequestException(pendingRequest.get());
        }
    }

    @Override
    public void cancelRequest(Player source) {
        sessionProvider.inSession(ignored -> {
            Optional<FriendRequest> sent = findSentRequest(source);
            if (!sent.isPresent()) {
                throw new NoRequestException();
            } else {
                doRevokeRequest(source, sent.get());
            }
        });
    }

    private void doRevokeRequest(Player source, FriendRequest request) {
        requestRepository.delete(request);
        playerService.findOnlinePlayer(request.getTarget().getUniqueId())
                .ifPresent(targetPlayer ->
                        I18n.sendLoc(targetPlayer, Format.broadcast("core!req-revoke", source.getName()))
                );
    }

    @Override
    public Friendship acceptRequest(FriendRequest request) {
        return sessionProvider.inSessionAnd(ignored -> {
            Preconditions.checkNotNull(request, "request");
            PlayerData source = request.getSource();
            PlayerData target = request.getTarget();
            checkExistingFriendship(target);
            Friendship friendship = friendshipRepository.create(source, target);
            notifyAccept(source.getUniqueId(), target.getUniqueId());
            notifyAccept(target.getUniqueId(), source.getUniqueId());
            requestRepository.delete(request);
            return friendship;
        });
    }

    private void notifyAccept(UUID receiverId, UUID otherId) {
        playerService.findOnlinePlayer(receiverId)
                .ifPresent(receiver -> {
                    String otherName = playerService.findNameFor(otherId, receiver);
                    I18n.sendLoc(receiver, Format.broadcast("core!friend.req-acc", otherName));
                });
    }
}
