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
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Collection;
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

    @Inject
    public HibernateFriendRequestService(FriendRequestRepository requestRepository, FriendshipRepository friendshipRepository,
                                         PlayerService playerService) {
        this.requestRepository = requestRepository;
        this.friendshipRepository = friendshipRepository;
        this.playerService = playerService;
    }

    @Override
    public Collection<FriendRequest> findReceivedRequests(PlayerData data) {
        return requestRepository.findReceivedRequests(data);
    }

    @Override
    public Optional<FriendRequest> findSentRequest(PlayerData data) {
        return requestRepository.findSentRequest(data);
    }

    @Override
    public FriendRequest requestFriendship(PlayerData source, PlayerData target) {
        Preconditions.checkNotNull(source, "source");
        Preconditions.checkNotNull(target, "target");
        checkSelfFriend(source, target);
        checkExistingFriendship(source);
        checkPendingRequest(source);
        FriendRequest request = requestRepository.create(source, target);
        playerService.findOnlinePlayer(target.getUniqueId())
                .ifPresent(targetPlayer -> notifyIncomingRequest(targetPlayer, source));
        return request;
    }

    private void notifyIncomingRequest(Player targetPlayer, PlayerData sourceData) {
        String name = playerService.findNameFor(sourceData.getUniqueId(), targetPlayer);
        ComponentSender.sendTo(
                targetPlayer,
                TextComponent.fromLegacyText(I18n.loc(targetPlayer, Format.broadcast("core!friend.req-inc", name))),
                new XyComponentBuilder(" ")
                        .append(I18n.loc(targetPlayer, "core!friend.req-inc-btn"))
                        .hintedCommand("/fs accept " + sourceData.getUniqueId())
                        .create()
        );
    }

    private void checkSelfFriend(PlayerData source, PlayerData target) {
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

    private void checkPendingRequest(PlayerData source) {
        Optional<FriendRequest> pendingRequest = findSentRequest(source);
        if (pendingRequest.isPresent()) {
            throw new PendingRequestException(pendingRequest.get());
        }
    }

    @Override
    public void cancelRequest(PlayerData source) {
        Optional<FriendRequest> sent = findSentRequest(source);
        if (!sent.isPresent()) {
            throw new NoRequestException();
        } else {
            FriendRequest request = sent.get();
            requestRepository.delete(request);
            playerService.findOnlinePlayer(request.getTarget().getUniqueId())
                    .ifPresent(targetPlayer -> {
                        String sourceName = playerService.findNameFor(source.getUniqueId(), targetPlayer);
                        I18n.sendLoc(targetPlayer, Format.broadcast("core!req-revoke", sourceName));
                    });
        }
    }

    @Override
    public Friendship acceptRequest(FriendRequest request) {
        Preconditions.checkNotNull(request, "request");
        PlayerData source = request.getSource();
        PlayerData target = request.getTarget();
        checkExistingFriendship(target);
        Friendship friendship = friendshipRepository.create(source, target);
        notifyAccept(source.getUniqueId(), target.getUniqueId());
        notifyAccept(target.getUniqueId(), source.getUniqueId());
        return friendship;
    }

    private void notifyAccept(UUID receiverId, UUID otherId) {
        playerService.findOnlinePlayer(receiverId)
                .ifPresent(receiver -> {
                    String otherName = playerService.findNameFor(otherId, receiver);
                    I18n.sendLoc(receiver, Format.broadcast("core!friend.req-acc", otherName));
                });
    }
}
