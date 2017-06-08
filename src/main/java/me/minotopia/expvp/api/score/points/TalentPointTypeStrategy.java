/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
