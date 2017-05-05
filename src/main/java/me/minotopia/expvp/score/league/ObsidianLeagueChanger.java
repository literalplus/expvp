/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.league;

import me.minotopia.expvp.api.model.PlayerData;

/**
 * Handles changing Obsidian players to Bedrock if they're in the top N players, and changing the replaced Bedrock
 * player back.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-05
 */
class ObsidianLeagueChanger extends ExpLeagueChanger {
    private final Top5Service top5Service;

    public ObsidianLeagueChanger(Top5Service top5Service) {
        super(StaticLeague.OBSIDIAN);
        this.top5Service = top5Service;
    }

    @Override
    public boolean needsLeagueChangeUp(PlayerData playerData) {
        return top5Service.isInTopFive(playerData);
    }
}
