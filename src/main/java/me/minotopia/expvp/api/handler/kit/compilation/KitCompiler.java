/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit.compilation;

import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.entity.Player;

/**
 * Compiles the appropriate kit for a player by resolving the list of kit items from handlers of
 * applicable skills into a list of item stacks.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
public interface KitCompiler {
    /**
     * Compiles a kit template for a specific player.
     *
     * @param player the player to compile for
     * @param data   the player data of given player
     * @return a template with the compiled item stacks
     */
    KitCompilation compile(Player player, PlayerData data);
}
