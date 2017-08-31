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

import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.points.TalentPointObjective;
import me.minotopia.expvp.api.score.points.TalentPointType;
import me.minotopia.expvp.api.score.points.TalentPointTypeStrategy;
import me.minotopia.expvp.score.points.objective.SimpleObjective;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Grants Talent Points if a specific amount of damage was dealt to other players recently.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public class DayActivityTalentPointStrategy implements TalentPointTypeStrategy {
    private final TalentPointType TYPE = TalentPointType.DAY_ACTIVITY;

    @Override
    public TalentPointType getType() {
        return TYPE;
    }

    @Override
    public int findDeservedPoints(MutablePlayerData playerData) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        switch (today) {
            case FRIDAY:
                return findPointsLeftUntil(playerData, 20);
            case SATURDAY:
                return findPointsLeftUntil(playerData, 40);
            default:
                return findPointsLeftUntil(playerData, 64);
        }
    }

    @Override
    public int getPointsUntilLimit(PlayerData playerData) {
        return findPointsLeftUntil(playerData, TYPE.getLimit());
    }

    private int findPointsLeftUntil(PlayerData playerData, int target) {
        return Math.max(0, target - playerData.getTalentPointCount(TYPE));
    }

    @Override
    public TalentPointObjective calculateObjectiveForNext(MutablePlayerData playerData) {
        return new SimpleObjective(Message.of("tp.obj.recent_damage"), TYPE);
    }
}
