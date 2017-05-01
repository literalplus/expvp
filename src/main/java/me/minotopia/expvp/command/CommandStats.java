/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.google.inject.Inject;
import li.l1t.common.chat.ComponentSender;
import li.l1t.common.chat.XyComponentBuilder;
import li.l1t.common.command.BukkitExecution;
import li.l1t.common.command.BukkitExecutionExecutor;
import li.l1t.common.exception.InternalException;
import li.l1t.common.exception.UserException;
import li.l1t.common.i18n.Message;
import li.l1t.common.shared.uuid.UUIDRepository;
import li.l1t.common.util.CommandHelper;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.friend.FriendService;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.model.RankService;
import me.minotopia.expvp.api.score.league.LeagueService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.Plurals;
import me.minotopia.expvp.i18n.exception.I18nInternalException;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import me.minotopia.expvp.util.SessionProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Displays a player's public stats.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-27
 */
@ConstructOnEnable
public class CommandStats extends BukkitExecutionExecutor {
    private final PlayerDataService players;
    private final UUIDRepository uuidRepository;
    private final FriendService friendService;
    private final DisplayNameService names;
    private final RankService rankService;
    private final LeagueService leagues;
    private final SessionProvider sessionProvider;

    @Inject
    public CommandStats(PlayerDataService players, UUIDRepository uuidRepository, FriendService friendService,
                        DisplayNameService names, RankService rankService, LeagueService leagues, SessionProvider sessionProvider,
                        EPPlugin plugin) {
        this.players = players;
        this.uuidRepository = uuidRepository;
        this.friendService = friendService;
        this.names = names;
        this.rankService = rankService;
        this.leagues = leagues;
        this.sessionProvider = sessionProvider;
        plugin.getCommand("stats").setExecutor(this);
    }

    @Override
    public boolean execute(BukkitExecution exec) throws UserException, InternalException {
        try {
            sessionProvider.inSession(ignored -> {
                PlayerData target = exec.findArg(0)
                        .map(this::tryFindTarget)
                        .orElseGet(provideSelfAsDataOrFail(exec.sender()));
                showStatsOfTo(target, exec.sender());
            });
        } catch (I18nInternalException | I18nUserException e) {
            I18n.sendLoc(exec.sender(), Message.of(e.getWrapperMessageKey(), Message.of(e.getMessageKey(), e.getMessageParameters())));
        } catch (InternalException | UserException e) {
            exec.sender().sendMessage(e.getColoredMessage());
        }
        return true;
    }

    private PlayerData tryFindTarget(String arg) {
        Optional<? extends PlayerData> target = Optional.ofNullable(uuidRepository.forName(arg))
                .flatMap(players::findData);
        if (target.isPresent()) {
            return target.get();
        } else {
            throw new I18nUserException("error!stats.unknown", arg);
        }
    }

    private Supplier<PlayerData> provideSelfAsDataOrFail(CommandSender sender) {
        return () -> {
            if (sender instanceof Player) {
                return players.findData(((Player) sender).getUniqueId())
                        .orElseThrow(() -> new I18nUserException("error!stats.unknown", sender.getName()));
            } else {
                throw new I18nUserException("error!stats.need-player");
            }
        };
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
                target.getExp(), Format.rank(rankService.getExpRank(target)), leagueName
        ));
    }

    private void showFriendInfo(PlayerData target, CommandSender receiver) {
        Optional<PlayerData> friend = friendService.findFriend(target);
        if (friend.isPresent()) {
            showFriendStatsTo(receiver, friend.get());
        } else if (!target.getUniqueId().equals(CommandHelper.getSenderId(receiver))) {
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
        I18n.sendLoc(receiver, Format.result("score!stats.best-streak-skills",
                target.getBestKillStreak(), target.getSkills().size()
        ));
    }
}
