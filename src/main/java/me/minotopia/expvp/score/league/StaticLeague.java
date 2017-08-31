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

package me.minotopia.expvp.score.league;

import me.minotopia.expvp.api.score.league.Capability;
import me.minotopia.expvp.api.score.league.League;
import org.bukkit.Material;

import java.util.Optional;

/**
 * Enumeration of the static leagues shipped with Expvp. The leagues are in order, i.e. a league's next league is always
 * the league with the ordinal one higher than that league.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-29
 */
public enum StaticLeague implements League {
    WOOD(-100, 1, 1, Material.WOOD),
    STONE(0, 1, 1, Material.STONE),
    EMERALD(200, 2, 2, Material.EMERALD_BLOCK),
    DIAMOND(300, 2, 3, Material.DIAMOND_BLOCK),
    OBSIDIAN(400, 2, 4, Material.OBSIDIAN),
    BEDROCK(OBSIDIAN.getMinExp(), 3, 5, Material.BEDROCK);

    private final int minExp;
    private final int killExpReward;
    private final int deathExpPenalty;
    private final Material displayMaterial;

    StaticLeague(int minExp, int killExpReward, int deathExpPenalty, Material displayMaterial) {
        this.minExp = minExp;
        this.killExpReward = killExpReward;
        this.deathExpPenalty = deathExpPenalty;
        this.displayMaterial = displayMaterial;
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

    @Override
    public Material getDisplayMaterial() {
        return displayMaterial;
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

    @Override
    public boolean may(Capability cap) {
        switch (cap) {
            case CHAT_COLOR:
                return isHigherOrEqualTo(StaticLeague.EMERALD);
            default:
                throw new UnsupportedOperationException("Unsupported capability " + cap);
        }
    }

    public boolean isHigherOrEqualTo(StaticLeague other) {
        return this.ordinal() >= other.ordinal();
    }
}
