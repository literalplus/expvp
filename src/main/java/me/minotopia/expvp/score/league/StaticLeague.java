/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.league;

import me.minotopia.expvp.api.score.league.League;

import java.util.Optional;

/**
 * Enumeration of the static leagues shipped with Expvp. The leagues are in order, i.e. a league's next league is always
 * the league with the ordinal one higher than that league.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-29
 */
public enum StaticLeague implements League {
    WOOD(-100, 1, 1),
    STONE(0, 1, 1),
    EMERALD(200, 2, 2),
    DIAMOND(300, 2, 3),
    OBSIDIAN(400, 2, 4),
    BEDROCK(OBSIDIAN.getMinExp(), 3, 5);

    private final int minExp;
    private final int killExpReward;
    private final int deathExpPenalty;

    StaticLeague(int minExp, int killExpReward, int deathExpPenalty) {
        this.minExp = minExp;
        this.killExpReward = killExpReward;
        this.deathExpPenalty = deathExpPenalty;
    }

    public static StaticLeague getDefaultLeague() {
        return WOOD;
    }

    @Override
    public int getMinExp() {
        return minExp;
    }

    @Override
    public int getKillExpReward() {
        return killExpReward;
    }

    @Override
    public int getDeathExpPenalty() {
        return deathExpPenalty;
    }

    public Optional<League> next() {
        int nextLeagueIndex = ordinal() + 1;
        if (nextLeagueIndex < StaticLeague.values().length) {
            return Optional.of(StaticLeague.values()[nextLeagueIndex]);
        } else {
            return Optional.empty();
        }
    }

    public Optional<League> previous() {
        int previousLeagueIndex = ordinal() - 1;
        if (previousLeagueIndex >= 0) {
            return Optional.of(StaticLeague.values()[previousLeagueIndex]);
        } else {
            return Optional.empty();
        }
    }
}
