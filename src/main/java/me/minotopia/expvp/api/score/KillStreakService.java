/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score;

import org.bukkit.entity.Player;

/**
 * Keeps track of current kill streaks of online players.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-27
 */
public interface KillStreakService {
    int getCurrentStreak(Player player);

    void increaseStreak(Player player);

    void resetStreak(Player player);
}
