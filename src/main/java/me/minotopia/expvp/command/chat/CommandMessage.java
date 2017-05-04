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
import li.l1t.common.util.UUIDHelper;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.chat.message.PMService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.misc.PlayerService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nInternalException;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

/**
 * Allows players to privately message each other.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
@ConstructOnEnable
public class CommandMessage extends BukkitExecutionExecutor {
    private final PMService pmService;
    private final PlayerService playerService;
    private final Server server;

    @Inject
    public CommandMessage(PMService pmService, EPPlugin plugin, PlayerService playerService, Server server) {
        this.pmService = pmService;
        this.playerService = playerService;
        this.server = server;
        plugin.getCommand("msg").setExecutor(this);
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
        if (exec.args().length < 2) {
            I18n.sendLoc(exec.sender(), "cmd!msg.usage");
        } else {

            pmService.sendMessage(exec.sender(), findReceiver(exec.arg(0)), exec.joinedArgsColored(1));
        }
    }

    private CommandSender findReceiver(String input) {
        if (input.equalsIgnoreCase("console")) {
            return server.getConsoleSender();
        } else if (UUIDHelper.isValidUUID(input)) {
            return playerService.findOnlinePlayer(UUIDHelper.getFromString(input))
                    .orElseThrow(() -> new I18nUserException("error!player-not-on"));
        } else {
            return playerService.findOnlinePlayer(input)
                    .orElseThrow(() -> new I18nUserException("error!no-such-player"));
        }
    }
}
