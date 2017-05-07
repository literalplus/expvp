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
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.chat.GlobalMuteService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.command.CommandSender;

/**
 * Clears the chat for everyone except those with a permission.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-07
 */
@ConstructOnEnable
public class CommandGlobalMute extends BukkitExecutionExecutor {
    private final GlobalMuteService globalMuteService;

    @Inject
    public CommandGlobalMute(EPPlugin plugin, GlobalMuteService globalMuteService) {
        this.globalMuteService = globalMuteService;
        plugin.getCommand("glomu").setExecutor(this);
    }

    @Override
    public boolean execute(BukkitExecution exec) throws UserException, InternalException {
        if (!hasPermission(exec.sender())) {
            I18n.sendLoc(exec.sender(), Format.userError("core!no-perm"));
        } else {
            globalMuteService.setEnabled(!globalMuteService.isEnabled());
            globalMuteService.setReason(reasonFromArgs(exec));
            I18n.sendLoc(exec.sender(), Format.success(statusMessage()));
        }
        return true;
    }

    private boolean hasPermission(CommandSender sender) {
        return Permission.CHAT_GLOBAL_MUTE.has(sender);
    }

    private String reasonFromArgs(BukkitExecution exec) {
        return exec.hasNoArgs() ? null : exec.joinedArgsColored(0);
    }

    private Message statusMessage() {
        return Message.of(globalMuteService.isEnabled() ? "admin!glomu.on" : "admin!glomu.off");
    }
}
