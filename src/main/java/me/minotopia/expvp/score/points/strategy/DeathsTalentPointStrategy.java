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
 * Grants Talent Points of type {@link TalentPointType#DEATHS} for deaths.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public class DeathsTalentPointStrategy implements TalentPointTypeStrategy {
    private static final TalentPointType TYPE = TalentPointType.DEATHS;
    private static final double SCALING_FACTOR = 1.39D;
    private static final double EXPONENT = 72D / 100D;
    private static final double EXPONENT_INVERSE = 100D / 72D;

    @Override
    public TalentPointType getType() {
        return TYPE;
    }

    @Override
    public int findDeservedPoints(MutablePlayerData playerData) {
        int pointsUntilLimit = getPointsUntilLimit(playerData);
        if (pointsUntilLimit > 0) {
            int alreadyGrantedPoints = playerData.getTalentPointCount(TalentPointType.COMBAT);
            int deservedPoints = totalDeservedPoints(playerData.getCurrentDeaths()) - alreadyGrantedPoints;
            if (deservedPoints > 0) {
                return deservedPoints;
            }
        }
        return 0;
    }

    private int totalDeservedPoints(int deathCount) {
        double deathsDouble = (double) deathCount;
        double rawPoints = SCALING_FACTOR * Math.pow(deathsDouble, EXPONENT);
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
                deathsLeftUntilNextPoint(playerData.getCurrentDeaths()),
                Plurals::deathPlural
        );
    }

    private int deathsLeftUntilNextPoint(int currentDeaths) {
        int currentTotalPoints = totalDeservedPoints(currentDeaths);
        int currentPointDeathLimit = upperDeathsLimitInTalentPoint(currentTotalPoints);
        int absoluteRequiredDeaths = currentPointDeathLimit + 1;
        return absoluteRequiredDeaths - currentDeaths;
    }

    private int upperDeathsLimitInTalentPoint(int talentPoints) {
        // this is the inverse of the deserved points function, with the n-th root simplified to an exponent
        double neededDeathsRaw = Math.pow(((double) talentPoints) / SCALING_FACTOR, EXPONENT_INVERSE);
        return toInt(Math.floor(neededDeathsRaw));
    }
}
