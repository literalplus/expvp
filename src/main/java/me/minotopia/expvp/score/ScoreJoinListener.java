/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

import com.google.inject.Inject;
import me.minotopia.expvp.api.reset.ResetService;
import me.minotopia.expvp.api.score.ExpService;
import me.minotopia.expvp.api.score.TalentPointService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

/**
 * Listens for join events and does some initialisation work for the score module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-07
 */
public class ScoreJoinListener implements Listener {
    private final ExpService exps;
    private final TalentPointService talentPoints;
    private final ResetService resetService;

    @Inject
    public ScoreJoinListener(ExpService exps, TalentPointService talentPoints, ResetService resetService) {
        this.exps = exps;
        this.talentPoints = talentPoints;
        this.resetService = resetService;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            exps.incrementExp(player, 100);
            talentPoints.grantTalentPoints(player, getFullDaysSinceReset() * 10);
        } else {
            player.setLevel(exps.getExpCount(player));
        }
    }

    private int getFullDaysSinceReset() {
        return Period.between(
                LocalDate.now().with(TemporalAdjusters.previousOrSame(resetService.getResetDay())),
                LocalDate.now()
        ).getDays();
    }
}
