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
import com.sk89q.intake.argument.MissingArgumentException;
import li.l1t.common.chat.ComponentSender;
import li.l1t.common.chat.XyComponentBuilder;
import li.l1t.common.i18n.Message;
import li.l1t.common.shared.uuid.UUIDRepository;
import li.l1t.common.util.CommandHelper;
import me.minotopia.expvp.api.friend.FriendService;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.model.RankService;
import me.minotopia.expvp.api.score.league.LeagueService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.Plurals;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import me.minotopia.expvp.util.SessionProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * Displays a player's public stats.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-27
 */
@AutoRegister("stats")
public class CommandStats {
    private final PlayerDataService players;
    private final UUIDRepository uuidRepository;
    private final FriendService friendService;
    private final DisplayNameService names;
    private final RankService rankService;
    private final LeagueService leagues;
    private final SessionProvider sessionProvider;

    @Inject
    public CommandStats(PlayerDataService players, UUIDRepository uuidRepository, FriendService friendService,
                        DisplayNameService names, RankService rankService, LeagueService leagues, SessionProvider sessionProvider) {
        this.players = players;
        this.uuidRepository = uuidRepository;
        this.friendService = friendService;
        this.names = names;
        this.rankService = rankService;
        this.leagues = leagues;
        this.sessionProvider = sessionProvider;
    }

    @Command(aliases = "", usage = "cmd!stats.root.usage", desc = "cmd!stats.root.desc")
    public void root(CommandSender sender, String arg) throws MissingArgumentException {
        sessionProvider.inSession(ignored -> {
            PlayerData target = tryFindTarget(sender, arg);
            showStatsOfTo(target, sender);
        });
    }

    private PlayerData tryFindTarget(CommandSender sender, String arg) {
        try {
            Optional<? extends PlayerData> target = findTargetFrom(sender, arg);
            if (target.isPresent()) {
                return target.get();
            } else {
                throw new I18nUserException("error!stats.unknown", arg);
            }
        } catch (MissingArgumentException e) {
            throw new AssertionError();
            // t h i s    c a n    n e v e r    h a p p e n
            // yet we still have to catch it because fuck checked exceptions
            // whoever designed this API sure must've hated clean code
        }
    }

    private Optional<? extends PlayerData> findTargetFrom(CommandSender sender, String arg) throws MissingArgumentException {
        if (arg == null) {
            return players.findData(CommandHelper.getSenderId(sender));
        } else {
            return Optional.ofNullable(uuidRepository.forName(arg))
                    .flatMap(players::findData);
        }
    }

    private void showStatsOfTo(PlayerData target, CommandSender receiver) {
        I18n.sendLoc(receiver, Format.header("score!stats.header", names.displayName(target)));
        showKillsDeathsTo(receiver, target.getCurrentKills(), target.getCurrentDeaths(), "score!stats.current-kds");
        showKillsDeathsTo(receiver, target.getTotalKills(), target.getTotalDeaths(), "score!stats.overall-kds");
        showOwnExpRelated(target, receiver);
        showFriendInfo(target, receiver);
        showStreakAndSkills(target, receiver);
    }

    private void showKillsDeathsTo(CommandSender receiver, int kills, int deaths, String messageKey) {
        double kdRatio = computeKDRatio(kills, deaths);
        I18n.sendLoc(receiver, Format.result(messageKey, Plurals.killPlural(kills), Plurals.deathPlural(deaths), kdRatio));
    }

    private double computeKDRatio(int kills, int deaths) {
        return (double) kills / (deaths == 0 ? 1D : (double) deaths);
    }

    private void showOwnExpRelated(PlayerData target, CommandSender receiver) {
        Message leagueName = names.displayName(leagues.getPlayerLeague(target));
        I18n.sendLoc(receiver, Format.result("score!stats.exp-related",
                target.getExp(), rankService.getExpRank(target), leagueName
        ));
    }

    private void showFriendInfo(PlayerData target, CommandSender receiver) {
        Optional<PlayerData> friend = friendService.findFriend(target);
        if (friend.isPresent()) {
            showFriendStatsTo(receiver, friend.get());
        } else {
            ComponentSender.sendTo(
                    receiver,
                    TextComponent.fromLegacyText(I18n.loc(receiver, Format.result("score!stats.no-friend"))),
                    new XyComponentBuilder(" ")
                            .append(I18n.loc(receiver, "score!stats.friend-add"))
                            .hintedCommand("/fs add " + target)
                            .color(ChatColor.DARK_GREEN).create()
            );
        }
    }

    private void showFriendStatsTo(CommandSender receiver, PlayerData friend) {
        double kdRatio = computeKDRatio(friend.getTotalKills(), friend.getTotalDeaths());
        I18n.sendLoc(receiver, Format.result("score!stats.friend-stats",
                names.displayName(friend), friend.getExp(), kdRatio
        ));
    }

    private void showStreakAndSkills(PlayerData target, CommandSender receiver) {
        I18n.sendLoc(receiver, "score!stats.best-streak-skills", target.getBestKillStreak(), target.getSkills().size());
    }
}
