/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import li.l1t.common.intake.provider.annotation.OnlinePlayer;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.api.friend.FriendRequest;
import me.minotopia.expvp.api.friend.FriendRequestService;
import me.minotopia.expvp.api.friend.FriendService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * User command to manage friendships.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-26
 */
@AutoRegister(value = "fs", aliases = "friend")
public class CommandFriend {
    private final FriendRequestService requestService;
    private final FriendService friendService;

    @Inject
    public CommandFriend(FriendRequestService requestService, FriendService friendService) {
        this.requestService = requestService;
        this.friendService = friendService;
    }

    @Command(aliases = "add", desc = "cmd!friend.add.desc", usage = "cmd!friend.add.usage")
    public void add(@Sender Player source, @OnlinePlayer Player target) {
        requestService.requestFriendship(source, target);
        I18n.sendLoc(source, Format.success("core!friend.req-sent"));
    }

    @Command(aliases = "end", desc = "cmd!friend.end.desc")
    public void end(@Sender Player source) {
        friendService.removeFriend(source);
    }

    @Command(aliases = "accept", desc = "cmd!friend.accept.desc", usage = "cmd!friend.accept.usage")
    public void accept(@Sender Player player, UUID sourceId) {
        FriendRequest request = requestService.findReceivedRequests(player).stream()
                .filter(req -> req.getSource().getUniqueId().equals(sourceId))
                .findFirst().orElseThrow(() -> new I18nUserException("core!friend.no-such-req"));
        requestService.acceptRequest(request);
    }

    @Command(aliases = "cancel", desc = "cmd!friend.cancel.desc")
    public void cancel(@Sender Player source) {
        requestService.cancelRequest(source);
        I18n.sendLoc(source, Format.success("core!friend.revoke"));
    }
}
