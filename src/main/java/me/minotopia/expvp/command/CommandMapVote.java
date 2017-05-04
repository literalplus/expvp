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