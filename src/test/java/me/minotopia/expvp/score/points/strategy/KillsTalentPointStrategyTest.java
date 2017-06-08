/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.points.strategy;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class KillsTalentPointStrategyTest {
    private final int currentKills;
    private final int expectedTP;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{0, 0}, {1, 2}, {17, 10}, {21, 11}, {30, 14}, {130, 41}});
    }

    public KillsTalentPointStrategyTest(int currentKills, int expectedTP) {
        this.currentKills = currentKills;
        this.expectedTP = expectedTP;
    }

    @Test
    public void testExpectedTP() throws Exception {
        //Given
        KillsTalentPointStrategy strategy = new KillsTalentPointStrategy();
        //When
        int actualDeservedPoints = strategy.totalDeservedPoints(currentKills);
        //Then
        Assert.assertThat("TP mismatch for " + currentKills, actualDeservedPoints, CoreMatchers.is(expectedTP));
    }

    @Test
    public void testPointsUntilNext() throws Exception {
        //Given
        KillsTalentPointStrategy strategy = new KillsTalentPointStrategy();
        int currentPoints = strategy.totalDeservedPoints(currentKills);
        //When
        int killsUntilNext = strategy.killsLeftUntilNextPoint(currentKills);
        //Then
        //System.out.printf("CURRENT %d kills = %d points -> %d until next", currentKills, currentPoints, killsUntilNext);
        int expectedKills = currentKills + killsUntilNext;
        for (int kills = currentKills; kills < expectedKills; kills++) {
            int points = strategy.totalDeservedPoints(kills);
            Assert.assertThat(
                    String.format("%d kills should be current, but is %d points (up to %d kills)",
                            kills, points, expectedKills),
                    points, CoreMatchers.is(currentPoints));
        }
        int actualPoints = strategy.totalDeservedPoints(expectedKills);
        if (currentPoints == 0) {
            Assert.assertThat(actualPoints, CoreMatchers.is(currentPoints + 2));
        } else {
            Assert.assertThat(actualPoints, CoreMatchers.is(currentPoints + 1));
        }
    }
}