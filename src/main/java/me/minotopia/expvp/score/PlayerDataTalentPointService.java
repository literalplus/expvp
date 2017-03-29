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
    private static final double TP_FINAL_FACTOR = 1.05D;
    private static final double TP_EXPONENT = 73D / 100D;
    private static final double TP_EXPONENT_INVERSE = 1000D / 73D;
    private static final double TP_DIVISOR = 0.885D;
    private final PlayerDataService players;
    private final SessionProvider sessionProvider;

    @Inject
    public PlayerDataTalentPointService(PlayerDataService players, SessionProvider sessionProvider) {
        this.players = players;
        this.sessionProvider = sessionProvider;
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
        int totalTalentPoints = findTotalDeservedTalentPoints(kills);
        return totalTalentPoints >= findTalentPointLimit(player);
    }

    private int findTotalDeservedTalentPoints(int killCount) {
        double rawPoints = TP_FINAL_FACTOR * Math.pow(killCount / TP_DIVISOR, TP_EXPONENT);
        return Double.valueOf(Math.ceil(rawPoints)).intValue();
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
            int newPoints = findTotalDeservedTalentPoints(playerData.getCurrentKills());
            if (newPoints > talentPointLimit) {
                return 0;
            } else {
                int previousPoints = findTotalDeservedTalentPoints(playerData.getCurrentKills() - 1);
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
        int currentTotalPoints = findTotalDeservedTalentPoints(currentKills);
        int currentPointMaxKills = findMaxKillsForTalentPoint(currentTotalPoints);
        int totalRequiredKills = currentPointMaxKills + 1;
        return totalRequiredKills - currentKills;
    }

    private int findMaxKillsForTalentPoint(int talentPoints) {
        // this is the inverse of the deserved points function, with the n-th root simplified to an exponent
        double neededKillsRaw = TP_DIVISOR * Math.pow((double) talentPoints / TP_FINAL_FACTOR, TP_EXPONENT_INVERSE);
        return Double.valueOf(Math.floor(neededKillsRaw)).intValue();
    }
}
