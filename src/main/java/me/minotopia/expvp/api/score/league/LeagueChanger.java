/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.league;

import me.minotopia.expvp.api.model.PlayerData;

/**
 * Finds out whether a league change is necessary for a player.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-29
 */
public interface LeagueChanger {
    boolean needsLeagueChangeUp(PlayerData playerData);

    boolean needsLeagueChangeDown(PlayerData playerData);
}
