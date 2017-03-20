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
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.command.permission.EnumRequires;
import me.minotopia.expvp.command.service.CommandService;
import me.minotopia.expvp.handler.kit.SkillKitService;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    private final SkillKitService skillKitService;

    @Inject
    CommandEPAdmin(CommandService commandService, SkillKitService skillKitService) {
        super(commandService);
        this.skillKitService = skillKitService;
    }

    @Command(aliases = "settp", min = 2,
            desc = "Setzt Talentpunkte",
            help = "Setzt die Talentpunkte\neines Spielers.",
            usage = "[uuid|name] [tp]")
    @EnumRequires(Permission.ADMIN_PLAYERS)
    public void setBooks(CommandSender sender, String playerSpec, int newTalentPoints)
            throws IOException {
        modifyProperty(sender, playerSpec, "Talentpunkte", playerData -> {
            playerData.setTalentPoints(newTalentPoints);
            return playerData.getTalentPoints();
        });
    }

    @Command(aliases = "addtp", min = 2,
            desc = "Gibt Talentpunkte",
            help = "Gibt einem Spieler Talentpunkte.",
            usage = "[uuid|name] [tp]")
    @EnumRequires(Permission.ADMIN_PLAYERS)
    public void addBooks(CommandSender sender, String playerSpec, int addTalentPoints)
            throws IOException {
        modifyProperty(sender, playerSpec, "Talentpunkte", playerData -> {
            playerData.setTalentPoints(playerData.getTalentPoints() + addTalentPoints);
            return playerData.getTalentPoints();
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
    public void whoIs(CommandSender sender, String playerInput)
            throws IOException {
        UUID playerId = service().findPlayerByNameOrIdOrFail(playerInput);
        PlayerData playerData = service().findPlayerData(playerId);
        sender.sendMessage("§a»»» §eSpielerinfo §a«««"); //TODO: player name -> xyc profile api
        formatMessage(sender,
                "§e§l➩ §eLiga: §a%d §eExp: §a%d §eTP: §a%d §eSprache: §a%s",
                playerData.getLeagueName(), playerData.getExp(), playerData.getTalentPoints(), playerData.getLocale().getDisplayName()
        );
        int totalKD = playerData.getTotalKills() / (playerData.getTotalDeaths() == 0 ? 1 : playerData.getTotalDeaths());
        formatMessage(sender,
                "§e§l➩ §aGesamte §eKills: §a%d §eDeaths: §a%d §eK/D: §a%d",
                playerData.getTotalKills(), playerData.getTotalDeaths(), totalKD
        );
        int currentKD = playerData.getCurrentKills() / (playerData.getCurrentDeaths() == 0 ? 1 : playerData.getCurrentDeaths());
        formatMessage(sender,
                "§e§l➩ §aAktuelle §eKills: §a%d §eDeaths: §a%d §eK/D: §a%d",
                playerData.getCurrentKills(), playerData.getCurrentDeaths(), currentKD
        );
        formatMessage(sender,
                "§e§l➩ §eSkills: §a%s",
                playerData.getSkills().stream().map(ObtainedSkill::getSkillId).collect(Collectors.joining())
        );
    }

    private void formatMessage(CommandSender sender, String format, Object... args) {
        sender.sendMessage(String.format(format, args));
    }

    @Command(aliases = "testkit", desc = "Testet dein Kit")
    @EnumRequires(Permission.ADMIN_BASIC)
    public void testKit(@Sender Player player) {
        skillKitService.invalidateCache(player.getUniqueId());
        skillKitService.applyKit(player);
        player.sendMessage("§eok sollte passen");
    }

    @Command(aliases = "clearcache", desc = "Leert diverse Caches")
    @EnumRequires(Permission.ADMIN_BASIC)
    public void clearCache(SessionProvider sessionProvider) {
        sessionProvider.getSessionFactory().getCache().evictAllRegions();
    }
}
