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