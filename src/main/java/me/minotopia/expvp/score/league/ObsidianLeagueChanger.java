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
 * @since 2017-04-02
 */
class ObsidianLeagueChanger extends ExpLeagueChanger {
    public ObsidianLeagueChanger() {
        super(StaticLeague.OBSIDIAN);
    }

    @Override
    public boolean needsLeagueChangeUp(PlayerData playerData) {
        /*
         TODO: Actual change to Bedrock, also needs a separate changer for changing back, because
         TODO: if a Bedrock player loses Exp and goes below an Obsidian player, that would be
         TODO: unhandled - abstract base class or something; https://bugs.l1t.li/view.php?id=786
          */
        return false;
    }
}
