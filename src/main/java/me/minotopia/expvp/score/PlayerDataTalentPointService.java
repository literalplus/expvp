/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.InsufficientTalentPointsException;
import me.minotopia.expvp.api.score.TalentPointService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

/**
 * Works out Talent Points using the PlayerData API.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
public class PlayerDataTalentPointService implements TalentPointService {
    private final PlayerDataService players;
    private final SessionProvider sessionProvider;
    private final TalentPointCalculator calculator;
    private final TalentPointDisplayService displayService;

    @Inject
    public PlayerDataTalentPointService(PlayerDataService players, SessionProvider sessionProvider,
                                        TalentPointCalculator calculator) {
        this.players = players;
        this.sessionProvider = sessionProvider;
        this.calculator = calculator;
        displayService = new TalentPointDisplayService(this);
    }

    @Override
    public int getCurrentTalentPointCount(Player player) {
        try (ScopedSession ignored = sessionProvider.scoped().join()) {
            return players.findData(player.getUniqueId())
                    .map(PlayerData::getTalentPoints)
                    .orElse(0);
        }
    }

    @Override
    public int findTalentPointLimit(Player player) {
        return 75; //TODO: Lanatus product for this maybe
    }

    @Override
    public boolean hasReachedTalentPointLimit(Player player) {
        Integer kills = players.findData(player.getUniqueId())
                .map(PlayerData::getCurrentKills)
                .orElse(0);
        return calculator.totalDeservedPoints(kills) >= findTalentPointLimit(player);
    }

    @Override
    public int grantTalentPointsForKill(Player player) {
        return sessionProvider.inSessionAnd(ignored -> {
            PlayerData playerData = players.findOrCreateData(player.getUniqueId());
            int talentPointLimit = findTalentPointLimit(player);
            int grantedTalentPoints = findDeservedTalentPointDifference(talentPointLimit, playerData.getCurrentKills());
            grantTalentPoints(player, grantedTalentPoints);
            displayService.displayTPGained(player, grantedTalentPoints);
            return grantedTalentPoints;
        });
    }

    private int findDeservedTalentPointDifference(int talentPointLimit, int currentKills) {
        if (currentKills <= 0) {
            return 0;
        } else {
            int newPoints = calculator.totalDeservedPoints(currentKills);
            if (newPoints > talentPointLimit) {
                return 0;
            } else {
                int previousPoints = calculator.totalDeservedPoints(currentKills - 1);
                return newPoints - previousPoints;
            }
        }
    }

    @Override
    public void grantTalentPoints(Player player, int talentPoints) {
        Preconditions.checkArgument(talentPoints >= 0, "talentPoints must be positive", talentPoints);
        if (talentPoints == 0) {
            return;
        }
        sessionProvider.inSession(ignored -> {
            MutablePlayerData data = players.findOrCreateDataMutable(player.getUniqueId());
            data.setTalentPoints(data.getTalentPoints() + talentPoints);
            players.saveData(data);
        });
    }

    @Override
    public void consumeTalentPoints(Player player, int consumePoints) {
        sessionProvider.inSession(ignored -> {
            MutablePlayerData playerData = players.findOrCreateDataMutable(player.getUniqueId());
            int currentPoints = playerData.getTalentPoints();
            if (currentPoints < consumePoints) {
                throw new InsufficientTalentPointsException(consumePoints, currentPoints);
            } else {
                playerData.setTalentPoints(currentPoints - consumePoints);
            }
            displayService.displayTPSpent(player, consumePoints);
        });
    }

    @Override
    public int findKillsUntilNextTalentPoint(Player player) {
        int currentKills = players.findData(player.getUniqueId())
                .map(PlayerData::getCurrentKills)
                .orElse(0);
        return calculator.killsLeftUntilNextPoint(currentKills);
    }

    @Override
    public void displayCurrentCount(Player player) {
        sessionProvider.inSession(ignored -> {
            displayService.displayCurrentTP(player);
        });
    }
}
