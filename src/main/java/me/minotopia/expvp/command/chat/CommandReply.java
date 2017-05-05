/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.chat;

import com.google.inject.Inject;
import li.l1t.common.command.BukkitExecution;
import li.l1t.common.command.BukkitExecutionExecutor;
import li.l1t.common.exception.InternalException;
import li.l1t.common.exception.UserException;
import li.l1t.common.util.CommandHelper;
import li.l1t.common.util.UUIDHelper;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.chat.message.PMService;
import me.minotopia.expvp.api.chat.message.ReplyService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.misc.PlayerService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nInternalException;
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
@ConstructOnEnable
public class CommandReply extends BukkitExecutionExecutor {
    private final PMService pmService;
    private final PlayerService playerService;
    private final ReplyService replyService;
    private final Server server;

    @Inject
    public CommandReply(PMService pmService, EPPlugin plugin, PlayerService playerService,
                        ReplyService replyService, Server server) {
        this.pmService = pmService;
        this.playerService = playerService;
        this.replyService = replyService;
        this.server = server;
        plugin.getCommand("reply").setExecutor(this);
    }

    @Override
    public boolean execute(BukkitExecution exec) throws UserException, InternalException {
        try {
            handle(exec);
        } catch (I18nUserException | I18nInternalException e) {
            I18n.sendLoc(exec.sender(), e.toMessage());
        }
        return true;
    }

    private void handle(BukkitExecution exec) {
        if (exec.args().length < 1) {
            I18n.sendLoc(exec.sender(), "cmd!reply.usage");
        } else {
            pmService.sendMessage(exec.sender(), findReceiver(exec.sender()), exec.joinedArgsColored(0));
        }
    }

    private CommandSender findReceiver(CommandSender sender) {
        UUID receiverId = replyService.getMostRecentPartner(CommandHelper.getSenderId(sender))
                .orElseThrow(() -> new I18nUserException("chat!reply.no-partner"));
        CommandSender receiver;
        if (receiverId.equals(UUIDHelper.NIL_UUID)) {
            receiver = server.getConsoleSender();
        } else {
            receiver = playerService.findOnlinePlayer(receiverId)
                    .orElseThrow(() -> new I18nUserException("chat!reply.no-partner"));
        }
        return receiver;
    }
}
