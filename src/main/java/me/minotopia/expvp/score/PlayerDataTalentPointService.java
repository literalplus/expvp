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

    @Inject
    public PlayerDataTalentPointService(PlayerDataService players, SessionProvider sessionProvider) {
        this.players = players;
        this.sessionProvider = sessionProvider;
    }

    @Override
    public int getCurrentTalentPointCount(Player player) {
        Integer talentPoints;
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            talentPoints = players.findData(player.getUniqueId()).map(PlayerData::getTalentPoints).orElse(0);
            scoped.commitIfLastAndChanged();
        }
        return talentPoints;
    }

    @Override
    public int findTalentPointLimit(Player player) {
        return 75; //TODO: Lanatus product for this maybe
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

    private int findTotalDeservedTalentPoints(int killCount) {
        return Double.valueOf(Math.ceil(1.05D * Math.pow(killCount / 0.885D, 0.73D))).intValue();
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
}
