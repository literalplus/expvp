/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class NextTalentPointCalculatorTest {
    private final int currentKills;
    private final int expectedTP;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{0, 0}, {1, 2}, {17, 10}, {21, 11}, {30, 14}, {130, 41}});
    }

    public NextTalentPointCalculatorTest(int currentKills, int expectedTP) {
        this.currentKills = currentKills;
        this.expectedTP = expectedTP;
    }

    @Test
    public void testExpectedTP() throws Exception {
        //Given
        TalentPointCalculator calc = new TalentPointCalculator();
        //When
        int actualDeservedPoints = calc.totalDeservedPoints(currentKills);
        //Then
        assertThat("TP mismatch for " + currentKills, actualDeservedPoints, is(expectedTP));
    }

    @Test
    public void testPointsUntilNext() throws Exception {
        //Given
        TalentPointCalculator calc = new TalentPointCalculator();
        int currentPoints = calc.totalDeservedPoints(currentKills);
        //When
        int killsUntilNext = calc.killsLeftUntilNextPoint(currentKills);
        //Then
        //System.out.printf("CURRENT %d kills = %d points -> %d until next", currentKills, currentPoints, killsUntilNext);
        int expectedKills = currentKills + killsUntilNext;
        for (int kills = currentKills; kills < expectedKills; kills++) {
            int points = calc.totalDeservedPoints(kills);
            assertThat(
                    String.format("%d kills should be current, but is %d points (up to %d kills)",
                            kills, points, expectedKills),
                    points, is(currentPoints));
        }
        int actualPoints = calc.totalDeservedPoints(expectedKills);
        if (currentPoints == 0) {
            assertThat(actualPoints, is(currentPoints + 2));
        } else {
            assertThat(actualPoints, is(currentPoints + 1));
        }
    }
}