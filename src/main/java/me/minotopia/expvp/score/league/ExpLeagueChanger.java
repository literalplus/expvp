/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.league;

import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.score.league.LeagueChanger;

import java.util.function.Function;

/**
 * Changes the league of players based solely on whether they have enough Exp.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-30
 */
class ExpLeagueChanger implements LeagueChanger {
    private final League league;

    public ExpLeagueChanger(League league) {
        this.league = league;
    }

    @Override
    public boolean needsLeagueChangeDown(PlayerData playerData) {
        return league.previous().isPresent() && playerData.getExp() < league.getMinExp();
    }

    @Override
    public boolean needsLeagueChangeUp(PlayerData playerData) {
        return league.next().map(shouldChangeUp(playerData)).orElse(false);
    }

    private Function<League, Boolean> shouldChangeUp(PlayerData playerData) {
        return higherLeague -> playerData.getExp() >= higherLeague.getMinExp();
    }
}
