/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.misc;

import org.bukkit.entity.Player;

/**
 * Repairs a player's kit.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-05
 */
public interface RepairService {
    void repair(Player player);
}
