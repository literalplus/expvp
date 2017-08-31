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

package me.minotopia.expvp.command.chat;

import com.google.inject.Inject;
import li.l1t.common.command.BukkitExecution;
import li.l1t.common.command.BukkitExecutionExecutor;
import li.l1t.common.exception.InternalException;
import li.l1t.common.exception.UserException;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Clears the chat for everyone except those with a permission.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-07
 */
@ConstructOnEnable
public class CommandChatClear extends BukkitExecutionExecutor {
    @Inject
    public CommandChatClear(EPPlugin plugin) {
        plugin.getCommand("cc").setExecutor(this);
    }

    @Override
    public boolean execute(BukkitExecution exec) throws UserException, InternalException {
        if (!hasCCPermission(exec.sender())) {
            I18n.sendLoc(exec.sender(), Format.userError("core!no-perm"));
            return true;
        }
        exec.sender().getServer().getOnlinePlayers()
                .forEach(player -> clearPlayerChatIfNotExempt(player, exec.sender()));
        return true;
    }

    private boolean hasCCPermission(CommandSender sender) {
        return Permission.CHAT_CLEAR.has(sender);
    }

    private void clearPlayerChatIfNotExempt(Player player, CommandSender culprit) {
        if (hasCCPermission(player)) {
            I18n.sendLoc(player, Format.broadcast("chat!cc.admin", culprit.getName()));
        } else {
            clearChatOf(player);
            I18n.sendLoc(player, Format.broadcast("chat!cc.broadcast"));
        }
    }

    private void clearChatOf(Player player) {
        for (int i = 0; i < 150; i++) {
            player.sendMessage(" §r ");     //Apparently this helps with hack clients' message combining stuffs which are apparently 9001% exploits
            player.sendMessage("    §3  "); //Even though one could just use the logs, but yeah, it looks professional or something. Yell at Janmm14.
        }
    }
}
