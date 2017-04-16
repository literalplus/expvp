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
import li.l1t.common.chat.ComponentSender;
import li.l1t.common.chat.XyComponentBuilder;
import li.l1t.common.intake.i18n.Message;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.api.spawn.SpawnVoteService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.Plurals;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

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
    private final SpawnService spawns;

    @Inject
    public CommandMapVote(SpawnVoteService voteService, DisplayNameService names, SpawnService spawns) {
        this.voteService = voteService;
        this.names = names;
        this.spawns = spawns;
    }

    @Command(aliases = "vote", min = 1,
            desc = "Wählt eine Map",
            usage = "[map]")
    public void vote(@Sender Player player, MapSpawn mapSpawn) throws IOException {
        voteService.castVoteFor(player.getUniqueId(), mapSpawn);
        I18n.sendLoc(player, Format.success("spawn!vote.voted", names.displayName(mapSpawn)));
    }

    @Command(aliases = "list",
            desc = "Listet die verfügbaren Maps auf",
            usage = "")
    public void list(CommandSender sender) {
        List<MapSpawn> spawns = this.spawns.getSpawns();
        if (spawns.isEmpty()) {
            throw new I18nUserException("spawn!vote.no-spawns");
        }
        I18n.sendLoc(sender, Format.listHeader("spawn!vote.header"));
        spawns.forEach(spawn -> sendSpawnItem(sender, spawn));
        I18n.sendLoc(sender, Format.result("spawn!vote.see-at-spawn"));
    }

    private void sendSpawnItem(CommandSender sender, MapSpawn spawn) {
        long voteCount = voteService.findVoteCount(spawn);
        Message itemMessage = Message.of("spawn!spawn-item",
                names.displayName(spawn), Plurals.plural("spawn!vote.vote", voteCount)
        );
        BaseComponent[] nameComponents = TextComponent.fromLegacyText(
                I18n.loc(sender, Format.listItem(itemMessage)) + " "
        );
        BaseComponent[] buttonComponents = new XyComponentBuilder(I18n.loc(sender, "spawn!spawn-button"))
                .color(ChatColor.GREEN)
                .command("/mv vote " + spawn.getId())
                .create();
        ComponentSender.sendTo(sender, nameComponents, buttonComponents);
    }
}