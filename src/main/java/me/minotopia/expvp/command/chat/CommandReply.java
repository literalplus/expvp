/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.chat;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import li.l1t.common.intake.provider.annotation.Colored;
import li.l1t.common.intake.provider.annotation.Merged;
import li.l1t.common.util.CommandHelper;
import li.l1t.common.util.UUIDHelper;
import me.minotopia.expvp.api.chat.message.PMService;
import me.minotopia.expvp.api.chat.message.ReplyService;
import me.minotopia.expvp.api.misc.PlayerService;
import me.minotopia.expvp.command.AutoRegister;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * Allows players to directly reply to previous messages.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
@AutoRegister(value = "reply", aliases = {"r"})
public class CommandReply {
    private final PMService pmService;
    private final ReplyService replyService;
    private final PlayerService playerService;
    private final Server server;

    @Inject
    public CommandReply(PMService pmService, ReplyService replyService, PlayerService playerService, Server server) {
        this.pmService = pmService;
        this.replyService = replyService;
        this.playerService = playerService;
        this.server = server;
    }

    @Command(aliases = "", desc = "cmd!msg.root.desc", min = 2)
    public void root(CommandSender sender, @Merged @Colored String message) {
        UUID receiverId = replyService.getMostRecentPartner(CommandHelper.getSenderId(sender))
                .orElseThrow(() -> new I18nUserException("chat!reply.no-partner"));
        CommandSender receiver;
        if (receiverId.equals(UUIDHelper.NIL_UUID)) {
            receiver = server.getConsoleSender();
        } else {
            receiver = playerService.findOnlinePlayer(receiverId)
                    .orElseThrow(() -> new I18nUserException("chat!reply.no-partner"));
        }
        pmService.sendMessage(sender, receiver, message);
    }
}
