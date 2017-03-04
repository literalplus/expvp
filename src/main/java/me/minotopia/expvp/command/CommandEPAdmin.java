/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.sk89q.intake.Command;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.command.permission.EnumRequires;
import me.minotopia.expvp.command.service.CommandService;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Command used to administer Expvp.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public class CommandEPAdmin extends AbstractServiceBackedCommand<CommandService> {
    @Command(aliases = "setbooks", min = 2,
            desc = "Setzt Skillpunkte",
            help = "Setzt die Skillpunkte\neines Spielers.",
            usage = "[uuid|name] [punkte]")
    @EnumRequires(Permission.ADMIN_PLAYERS)
    public void setBooks(CommandSender sender, String playerSpec, int newBooks)
            throws IOException {
        modifyProperty(sender, playerSpec, "Skillpunkte", playerData -> {
            playerData.setBooks(newBooks);
            return playerData.getBooks();
        });
    }

    @Command(aliases = "addbooks", min = 2,
            desc = "Gibt Skillpunkte",
            help = "Gibt einem Spieler Skillpunkte.",
            usage = "[uuid|name] [punkte]")
    @EnumRequires(Permission.ADMIN_PLAYERS)
    public void addBooks(CommandSender sender, String playerSpec, int addBooks)
            throws IOException {
        modifyProperty(sender, playerSpec, "Skillpunkte", playerData -> {
            playerData.setBooks(playerData.getBooks() + addBooks);
            return playerData.getBooks();
        });
    }

    @Command(aliases = "setmelons", min = 2,
            desc = "Setzt Melonen",
            help = "Setzt die Melonen\neines Spielers.",
            usage = "[uuid|name] [melonen]")
    @EnumRequires(Permission.ADMIN_PLAYERS)
    public void setMelons(CommandSender sender, String playerSpec, int newMelons)
            throws IOException {
        modifyProperty(sender, playerSpec, "Melonen", playerData -> {
            playerData.setMelons(newMelons);
            return playerData.getMelons();
        });
    }

    @Command(aliases = "addmelons", min = 2,
            desc = "Gibt Melonen",
            help = "Gibt einem Spieler Melonen.",
            usage = "[uuid|name] [melonen]")
    @EnumRequires(Permission.ADMIN_PLAYERS)
    public void addMelons(CommandSender sender, String playerSpec, int addMelons)
            throws IOException {
        modifyProperty(sender, playerSpec, "Melonen", playerData -> {
            playerData.setMelons(playerData.getMelons() + addMelons);
            return playerData.getMelons();
        });
    }

    private void modifyProperty(CommandSender sender, String playerInput, String property,
                                Function<MutablePlayerData, Integer> mutator) {
        UUID playerId = service().findPlayerByNameOrIdOrFail(playerInput);
        int newValue = service().modifyPlayerData(playerId, mutator);
        sender.sendMessage(String.format( //TODO: player name -> xyc profile api
                "§e§l➩ §aDieser Spieler hat jetzt %d " + property + ".",
                newValue
        ));
    }

    @Command(aliases = "whois", min = 2,
            desc = "Zeigt Infos zu einem Spieler",
            usage = "[uuid|name]")
    @EnumRequires(Permission.ADMIN_BASIC)
    public void whoIs(CommandSender sender, String playerInput, int newBooks)
            throws IOException {
        UUID playerId = service().findPlayerByNameOrIdOrFail(playerInput);
        PlayerData playerData = service().findPlayerData(playerId);
        sender.sendMessage("§a»»» §eSpielerinfo §a«««"); //TODO: player name -> xyc profile api
        formatMessage(sender,
                "§e§l➩ §eLevel: §a%d §ePunkte: §a%d §eSkillpunkte: §a%d",
                playerData.getLevel(), playerData.getPoints(), playerData.getBooks()
        );
        formatMessage(sender,
                "§e§l➩ §eKills: §a%d §eDeaths: §a%d §eMelonen: §a%d",
                playerData.getKills(), playerData.getDeaths(), playerData.getMelons()
        );
        formatMessage(sender,
                "§e§l➩ §eSkills: §a%s",
                playerData.getSkills().stream().map(ObtainedSkill::getSkillId).collect(Collectors.joining())
        );
    }

    private void formatMessage(CommandSender sender, String format, Object... args) {
        sender.sendMessage(String.format(format, args));
    }
}
