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
}
