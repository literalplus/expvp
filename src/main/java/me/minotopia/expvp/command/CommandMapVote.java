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
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnVoteService;
import me.minotopia.expvp.command.permission.EnumRequires;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * Command that allows players to vote for maps.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-16
 */
@AutoRegister(value = "mv", aliases = "cast")
public class CommandMapVote {
    private final SpawnVoteService voteService;
    private final DisplayNameService names;

    @Inject
    public CommandMapVote(SpawnVoteService voteService, DisplayNameService names) {
        this.voteService = voteService;
        this.names = names;
    }

    @Command(aliases = "vote", min = 1,
            desc = "Wählt eine Map",
            usage = "[map]")
    @EnumRequires(Permission.COMMAND_MAP_VOTE)
    public void vote(@Sender Player player, MapSpawn mapSpawn) throws IOException {
        voteService.castVoteFor(player.getUniqueId(), mapSpawn);
        I18n.sendLoc(player, Format.success("spawn!vote.voted", names.displayName(mapSpawn)));
    }

    @Command(aliases = "list",
            desc = "Listet die verfügbaren Maps auf",
            usage = "")
    public void list(CommandSender sender) {
        voteService.showCurrentVotesTo(sender, Permission.COMMAND_MAP_VOTE.has(sender));
    }
}