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

import me.minotopia.expvp.api.model.PlayerData;

/**
 * Handles changing Bedrock players to Obsidian if they're out of the top N players, and changing the replaced Bedrock
 * player back.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-05
 */
class BedrockLeagueChanger extends ExpLeagueChanger {
    private final Top5Service top5Service;

    public BedrockLeagueChanger(Top5Service top5Service) {
        super(StaticLeague.BEDROCK);
        this.top5Service = top5Service;
    }

    @Override
    public boolean needsLeagueChangeDown(PlayerData playerData) {
        return !top5Service.isInTopFive(playerData);
    }

    @Override
    public boolean needsLeagueChangeUp(PlayerData playerData) {
        return false;
    }
}
