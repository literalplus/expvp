/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.service;

import org.bukkit.entity.Player;

/**
 * Handles behaviour executed when a player fatally hits another player.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-29
 */
public interface KillDeathService {
    void onFatalHit(Player culprit, Player victim);
}
