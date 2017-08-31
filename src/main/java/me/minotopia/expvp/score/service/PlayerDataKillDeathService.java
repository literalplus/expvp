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

package me.minotopia.expvp.score.service;

import com.google.inject.Inject;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.friend.FriendService;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.misc.PlayerService;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.assist.KillAssistService;
import me.minotopia.expvp.api.score.hit.HitList;
import me.minotopia.expvp.api.score.league.LeagueService;
import me.minotopia.expvp.api.score.points.TalentPointObjective;
import me.minotopia.expvp.api.score.points.TalentPointService;
import me.minotopia.expvp.api.score.points.TalentPointType;
import me.minotopia.expvp.api.score.service.ExpService;
import me.minotopia.expvp.api.score.service.KillDeathService;
import me.minotopia.expvp.api.score.service.KillStreakService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.Plurals;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

/**
 * Handles players fatally hitting other players using the PlayerData API and related services.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-29
 */
public class PlayerDataKillDeathService implements KillDeathService {
    private final TalentPointService talentPoints;
    private final SessionProvider sessionProvider;
    private final PlayerDataService players;
    private final LeagueService leagues;
    private final ExpService exps;
    private final DisplayNameService names;
    private final KillStreakService streakService;
    private final KillAssistService assistService;
    private final FriendService friendService;
    private final PlayerService playerService;

    @Inject
    public PlayerDataKillDeathService(TalentPointService talentPoints, SessionProvider sessionProvider,
                                      PlayerDataService players, LeagueService leagues, ExpService exps,
                                      DisplayNameService names, KillStreakService streakService,
                                      KillAssistService assistService, FriendService friendService,
                                      PlayerService playerService) {
        this.talentPoints = talentPoints;
        this.sessionProvider = sessionProvider;
        this.players = players;
        this.leagues = leagues;
        this.exps = exps;
        this.names = names;
        this.streakService = streakService;
        this.assistService = assistService;
        this.friendService = friendService;
        this.playerService = playerService;
    }

    @Override
    public void onFatalHit(Player culprit, Player victim) {
        sessionProvider.inSession(ignored -> {
            recordKill(culprit);
            recordDeath(victim);
            friendService.findFriend(culprit)
                    .ifPresent(friendData -> attemptRecordKillAssist(victim, friendData));
            sendKillBroadcast(culprit, victim);
        });
    }

    private void sendKillBroadcast(Player culprit, Player victim) {
        victim.getServer().getOnlinePlayers()
                .forEach(player -> I18n.sendLoc(player, Format.broadcast(Message.of(
                        "score!kill.broadcast",
                        names.displayName(culprit),
                        names.displayName(victim)
                ))));
    }

    private void recordDeath(Player victim) {
        MutablePlayerData playerData = players.findOrCreateDataMutable(victim.getUniqueId());
        playerData.addDeath();
        int expPenalty = leagues.getCurrentLeague(victim).getDeathExpPenalty();
        exps.decrementExp(victim, expPenalty);
        I18n.sendLoc(victim, Format.result(Message.of("score!kill.victim", expPenalty)));
        grantTalentPointsToVictim(victim);
    }

    private void grantTalentPointsToVictim(Player victim) {
        grantDeservedTalentPoints(victim, TalentPointType.DEATHS);
    }

    private void grantDeservedTalentPoints(Player target, TalentPointType type) {
        int grantedTalentPoints = talentPoints.grantDeservedTalentPoints(target, type);
        if (grantedTalentPoints > 0) {
            showNextTalentPointObjective(target, type);
        }
    }

    private void showNextTalentPointObjective(Player culprit, TalentPointType type) {
        int currentTP = talentPoints.getCurrentTalentPointCount(culprit);
        TalentPointObjective objective = talentPoints.nextPointObjective(culprit, type);
        I18n.sendLoc(culprit, Format.success(
                Message.of("score!tp.status",
                        Plurals.talentPointPlural(currentTP),
                        objective.getDescription()
                )
        ));
    }

    private void recordKill(Player culprit) {
        MutablePlayerData playerData = players.findOrCreateDataMutable(culprit.getUniqueId());
        playerData.addKill();
        int expReward = doGrantKillRewards(culprit);
        I18n.sendLoc(culprit, Format.result(Message.of("score!kill.culprit", expReward)));
        grantTalentPointsToKiller(culprit);
        streakService.increaseStreak(culprit);
    }

    private int doGrantKillRewards(Player culprit) {
        int expReward = leagues.getCurrentLeague(culprit).getKillExpReward();
        exps.incrementExp(culprit, expReward);
        return expReward;
    }

    private void grantTalentPointsToKiller(Player culprit) {
        grantDeservedTalentPoints(culprit, TalentPointType.COMBAT);
    }

    private void attemptRecordKillAssist(Player victim, PlayerData friend) {
        HitList hitList = assistService.getHitsOn(victim.getUniqueId())
                .getHitList(friend.getUniqueId());
        double recentDamageSum = hitList.getRecentDamageSum();
        if (recentDamageSum >= 6) {
            playerService.findOnlinePlayer(friend.getUniqueId())
                    .ifPresent(this::recordKillAssist);
        }
    }

    private void recordKillAssist(Player assistant) {
        MutablePlayerData playerData = players.findOrCreateDataMutable(assistant.getUniqueId());
        playerData.addKillAssist();
        int expReward = doGrantKillRewards(assistant);
        I18n.sendLoc(assistant, Format.result(Message.of("score!kill.assist", expReward)));
    }
}
