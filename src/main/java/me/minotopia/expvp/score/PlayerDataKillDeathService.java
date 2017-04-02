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
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.score.KillDeathService;
import me.minotopia.expvp.api.score.TalentPointService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.Server;
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

    @Inject
    public PlayerDataKillDeathService(TalentPointService talentPoints, SessionProvider sessionProvider, PlayerDataService players) {
        this.talentPoints = talentPoints;
        this.sessionProvider = sessionProvider;
        this.players = players;
    }

    @Override
    public void onFatalHit(Player culprit, Player victim) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            broadcastKill(culprit.getServer());
            recordKill(culprit);
            recordDeath(victim);
            //FIXME: Exp/leagues
            scoped.commitIfLastAndChanged();
        }
    }

    private void broadcastKill(Server server) {
        server.getOnlinePlayers()
                .forEach(player -> I18n.sendLoc(player, Format.broadcast(Message.of("core!kill.broadcast"))));
    }

    private void recordDeath(Player victim) {
        MutablePlayerData playerData = players.findOrCreateDataMutable(victim.getUniqueId());
        playerData.addDeath();
    }

    private void recordKill(Player culprit) {
        MutablePlayerData playerData = players.findOrCreateDataMutable(culprit.getUniqueId());
        playerData.addKill();
        int grantedTalentPoints = talentPoints.grantTalentPointsForKill(culprit);
        if (grantedTalentPoints == 1) {
            I18n.sendLoc(culprit, Format.resultSuccess(Message.of("score!tp.kill.one")));
        } else if (grantedTalentPoints > 1) {
            I18n.sendLoc(culprit, Format.resultSuccess(Message.of("score!kill.tp.many", grantedTalentPoints)));
        }
    }
}
