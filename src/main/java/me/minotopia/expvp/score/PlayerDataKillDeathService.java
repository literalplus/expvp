/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

import com.google.inject.Inject;
import li.l1t.common.intake.i18n.Message;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.score.ExpService;
import me.minotopia.expvp.api.score.KillDeathService;
import me.minotopia.expvp.api.score.TalentPointService;
import me.minotopia.expvp.api.score.league.LeagueService;
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

    @Inject
    public PlayerDataKillDeathService(TalentPointService talentPoints, SessionProvider sessionProvider,
                                      PlayerDataService players, LeagueService leagues, ExpService exps,
                                      DisplayNameService names) {
        this.talentPoints = talentPoints;
        this.sessionProvider = sessionProvider;
        this.players = players;
        this.leagues = leagues;
        this.exps = exps;
        this.names = names;
    }

    @Override
    public void onFatalHit(Player culprit, Player victim) {
        sessionProvider.inSession(ignored -> {
            recordKill(culprit);
            recordDeath(victim);
            sendKillBroadcast(culprit, victim);
        });
    }

    private void sendKillBroadcast(Player culprit, Player victim) {
        victim.getServer().getOnlinePlayers()
                .forEach(player -> I18n.sendLoc(player, Format.broadcast(Message.of(
                        "score!kill.broadcast",
                        names.displayName(victim),
                        names.displayName(culprit)
                ))));
    }

    private void recordDeath(Player victim) {
        MutablePlayerData playerData = players.findOrCreateDataMutable(victim.getUniqueId());
        playerData.addDeath();
        int expPenalty = leagues.getCurrentLeague(victim).getDeathExpPenalty();
        exps.decrementExp(victim, expPenalty);
        I18n.sendLoc(victim, Format.result(Message.of("score!kill.victim", expPenalty)));
    }

    private void recordKill(Player culprit) {
        MutablePlayerData playerData = players.findOrCreateDataMutable(culprit.getUniqueId());
        playerData.addKill();
        int expReward = leagues.getCurrentLeague(culprit).getKillExpReward();
        exps.incrementExp(culprit, expReward);
        I18n.sendLoc(culprit, Format.result(Message.of("score!kill.culprit", expReward)));
        grantTalentPointsToKiller(culprit);
    }

    private void grantTalentPointsToKiller(Player culprit) {
        int grantedTalentPoints = talentPoints.grantTalentPointsForKill(culprit);
        if (grantedTalentPoints > 0) {
            int currentTP = talentPoints.getCurrentTalentPointCount(culprit);
            int killsUntilNextTP = talentPoints.findKillsUntilNextTalentPoint(culprit);
            I18n.sendLoc(culprit, Format.success(
                    Message.of("score!tp.status",
                            Plurals.talentPointPlural(currentTP),
                            Plurals.killPlural(killsUntilNextTP)
                    )
            ));
        }
    }
}
