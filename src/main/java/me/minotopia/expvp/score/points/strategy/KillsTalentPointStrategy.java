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
