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

package me.minotopia.expvp.api.model;

import me.minotopia.expvp.api.score.points.TalentPointType;

/**
 * Represents a player's current Talent Point count for a specific type. These are used to implement limits per type.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-01
 */
public interface PlayerPoints {
    PlayerData getPlayerData();

    TalentPointType getType();

    int getCurrentPointCount();

    void increasePointCountBy(int modifier);
}
