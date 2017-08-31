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

package me.minotopia.expvp.api.score.league;

import org.bukkit.Material;

import java.util.Optional;

/**
 * Represents a class of players that players are sorted into by their Exp.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-29
 */
public interface League {
    String name();

    int getMinExp();

    int getKillExpReward();

    int getDeathExpPenalty();

    Material getDisplayMaterial();

    /**
     * @return the next league higher than this league
     */
    Optional<League> next();

    /**
     * @return the previous league lower than this league
     */
    Optional<League> previous();

    boolean may(Capability cap);
}
