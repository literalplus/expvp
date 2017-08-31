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

package me.minotopia.expvp.api.score.points;

import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;

/**
 * Calculates Talent Point grants for a specific type of talent point.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public interface TalentPointTypeStrategy {
    TalentPointType getType();

    /**
     * Finds the amount of Talent Points given player currently deserves additionally to their current amount of Talent
     * Points.
     *
     * @param playerData the player to operate on
     * @return the positive amount of extra Talent Points given player deserves additionally, or zero for none
     */
    int findDeservedPoints(MutablePlayerData playerData);

    /**
     * @param playerData the player to inspect
     * @return the amount of Talent Points given player can still receive from this strategy's type based on Talent
     * Point type limits, zero if none or above the limit
     */
    int getPointsUntilLimit(PlayerData playerData);

    /**
     * @param playerData the player to inspect
     * @return the objective given player needs to complete before they will get their next Talent Point according to
     * this strategy
     */
    TalentPointObjective calculateObjectiveForNext(MutablePlayerData playerData);
}
