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

import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Assigns players to the appropriate leagues and distributes assignment information.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-02
 */
public interface LeagueService {
    /**
     * @param player the player whose league to find
     * @return the league of given player, or the default league if the player does not exist or the player's league is
     * invalid
     */
    League getCurrentLeague(Player player);

    League getPlayerLeague(PlayerData playerData);

    /**
     * @param leagueName the name of the league to get
     * @return an optional containing the league of given name, or an empty optional if there is no such league
     */
    Optional<League> getLeague(String leagueName);

    /**
     * Updates given player's league based on their current scores, if necessary.
     *
     * @param player the player whose league to update
     */
    void updateLeague(Player player);
}
