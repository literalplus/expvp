/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.league;

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
