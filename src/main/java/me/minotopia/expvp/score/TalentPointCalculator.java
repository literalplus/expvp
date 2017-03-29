/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

/**
 * Executes computations related to Talent Points, such as calculating the amount of Talent Points for a specific kill
 * count.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-29
 */
public class TalentPointCalculator {
    private static final double SCALING_FACTOR = 1.05D;
    private static final double EXPONENT = 73D / 100D;
    private static final double EXPONENT_INVERSE = 100D / 73D;
    private static final double DIRECT_DIVISOR = 0.885D;

    public int totalDeservedPoints(int killCount) {
        double killsDouble = (double) killCount;
        double rawPoints = SCALING_FACTOR * Math.pow(killsDouble / DIRECT_DIVISOR, EXPONENT);
        return toInt(Math.ceil(rawPoints));
    }

    private int toInt(double value) {
        return Double.valueOf(value).intValue();
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
