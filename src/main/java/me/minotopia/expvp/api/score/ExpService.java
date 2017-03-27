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
 * Handles changing players' Exp and notifying relevant components.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
public interface ExpService {
    void incrementExp(Player player, int exp);

    void decrementExp(Player player, int exp);

    int getExpCount(Player player);
}
