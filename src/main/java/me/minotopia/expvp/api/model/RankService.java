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

/**
 * Computes players' ranks in various categories.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-27
 */
public interface RankService {
    /**
     * Force-fetches a player's rank by Exp from the database, even if there is one cached.
     *
     * @param data the player to find the exp rank for
     * @return the amount of players that have more Exp than given player, plus one
     */
    long findExpRank(PlayerData data);

    /**
     * @param data the player to find the exp rank for
     * @return the {@link #findExpRank(PlayerData) fetched exp rank} for given player, or a short-term cached value, if
     * available
     */
    long getExpRank(PlayerData data);
}
