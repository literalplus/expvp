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

    @Inject
    public PlayerDataTalentPointService(PlayerDataService players, SessionProvider sessionProvider,
                                        TalentPointCalculator calculator) {
        this.players = players;
        this.sessionProvider = sessionProvider;
        this.calculator = calculator;
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
        int grantedTalentPoints;
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            MutablePlayerData playerData = players.findOrCreateDataMutable(player.getUniqueId());
            int talentPointLimit = findTalentPointLimit(player);
            grantedTalentPoints = grantDeservedTalentPoints(playerData, talentPointLimit);
            scoped.commitIfLastAndChanged();
        }
        return grantedTalentPoints;
    }

    private int grantDeservedTalentPoints(MutablePlayerData playerData, int talentPointLimit) {
        if (playerData.getCurrentKills() <= 0) {
            return 0;
        } else {
            int newPoints = calculator.totalDeservedPoints(playerData.getCurrentKills());
            if (newPoints > talentPointLimit) {
                return 0;
            } else {
                int previousPoints = calculator.totalDeservedPoints(playerData.getCurrentKills() - 1);
                return grantTalentPointDifference(playerData, newPoints, previousPoints);
            }
        }
    }

    private int grantTalentPointDifference(MutablePlayerData playerData, int newPoints, int previousPoints) {
        Preconditions.checkArgument(newPoints >= previousPoints,
                "new must not be less than previous points, for: ",
                newPoints, previousPoints, playerData);
        int difference = newPoints - previousPoints;
        if (difference > 0) {
            playerData.setTalentPoints(playerData.getTalentPoints() + difference);
            players.saveData(playerData);
        }
        return difference;
    }

    @Override
    public void consumeTalentPoints(Player player, int consumePoints) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            MutablePlayerData playerData = players.findOrCreateDataMutable(player.getUniqueId());
            int currentPoints = playerData.getTalentPoints();
            if (currentPoints < consumePoints) {
                throw new InsufficientTalentPointsException(consumePoints, currentPoints);
            } else {
                playerData.setTalentPoints(currentPoints - consumePoints);
            }
            scoped.commitIfLastAndChanged();
        }
    }

    @Override
    public int findKillsUntilNextTalentPoint(Player player) {
        int currentKills = players.findData(player.getUniqueId())
                .map(PlayerData::getCurrentKills)
                .orElse(0);
        return calculator.killsLeftUntilNextPoint(currentKills);
    }
}
