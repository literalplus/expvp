/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.points.strategy;

import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.points.TalentPointObjective;
import me.minotopia.expvp.api.score.points.TalentPointType;
import me.minotopia.expvp.api.score.points.TalentPointTypeStrategy;
import me.minotopia.expvp.i18n.Plurals;
import me.minotopia.expvp.score.points.objective.SimpleScoreObjective;

/**
 * Grants Talent Points of type {@link TalentPointType#COMBAT} for kills.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public class KillsTalentPointStrategy implements TalentPointTypeStrategy {
    private static final TalentPointType TYPE = TalentPointType.COMBAT;
    private static final double SCALING_FACTOR = 1.05D;
    private static final double EXPONENT = 73D / 100D;
    private static final double EXPONENT_INVERSE = 100D / 73D;
    private static final double DIRECT_DIVISOR = 0.885D;

    @Override
    public TalentPointType getType() {
        return TYPE;
    }

    @Override
    public int findDeservedPoints(MutablePlayerData playerData) {
        int pointsUntilLimit = getPointsUntilLimit(playerData);
        if (pointsUntilLimit > 0) {
            int alreadyGrantedPoints = playerData.getTalentPointCount(TalentPointType.COMBAT);
            int deservedPoints = totalDeservedPoints(playerData.getCurrentKills()) - alreadyGrantedPoints;
            if (deservedPoints > 0) {
                return deservedPoints;
            }
        }
        return 0;
    }

    public int totalDeservedPoints(int killCount) {
        double killsDouble = (double) killCount;
        double rawPoints = SCALING_FACTOR * Math.pow(killsDouble / DIRECT_DIVISOR, EXPONENT);
        return toInt(Math.ceil(rawPoints));
    }

    private int toInt(double value) {
        return Double.valueOf(value).intValue();
    }

    @Override
    public int getPointsUntilLimit(PlayerData playerData) {
        return Math.max(0, TYPE.getLimit() - playerData.getTalentPointCount(TYPE));
    }

    @Override
    public TalentPointObjective calculateObjectiveForNext(MutablePlayerData playerData) {
        return new SimpleScoreObjective(
                TYPE,
                killsLeftUntilNextPoint(playerData.getCurrentKills()),
                Plurals::killPlural
        );
    }

    public int killsLeftUntilNextPoint(int currentKills) {
        int currentTotalPoints = totalDeservedPoints(currentKills);
        int currentPointKillLimit = upperKillsLimitInTalentPoint(currentTotalPoints);
        int absoluteRequiredKills = currentPointKillLimit + 1;
        return absoluteRequiredKills - currentKills;
    }

    private int upperKillsLimitInTalentPoint(int talentPoints) {
        // this is the inverse of the deserved points function, with the n-th root simplified to an exponent
        double neededKillsRaw = DIRECT_DIVISOR * Math.pow(((double) talentPoints) / SCALING_FACTOR, EXPONENT_INVERSE);
        return toInt(Math.floor(neededKillsRaw));
    }
}
