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

package me.minotopia.expvp.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.button.VoteButtonService;
import me.minotopia.expvp.command.permission.EnumRequires;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.entity.Player;

/**
 * Allows players to link buttons to vote for spawns.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-19
 */
@AutoRegister("splink")
public class CommandSpawnLink {
    private final VoteButtonService buttons;

    @Inject
    public CommandSpawnLink(VoteButtonService buttons) {
        this.buttons = buttons;
    }

    @Command(aliases = "link",
            desc = "cmd!splink.link.desc", help = "cmd!splink.link.help",
            usage = "cmd!splink.link.usage", min = 1)
    @EnumRequires(Permission.ADMIN_SPAWN)
    public void link(@Sender Player player, MapSpawn spawn) {
        buttons.startLinkingSession(player, spawn);
        I18n.sendLoc(player, Format.success("admin!spawn.link.session-start"));
    }

    @Command(aliases = "unlink",
            desc = "cmd!splink.link.desc", help = "cmd!splink.link.help")
    @EnumRequires(Permission.ADMIN_SPAWN)
    public void unlink(@Sender Player player) {
        buttons.startLinkingSession(player, null);
        I18n.sendLoc(player, Format.success("admin!spawn.unlink.session-start"));
    }
}
