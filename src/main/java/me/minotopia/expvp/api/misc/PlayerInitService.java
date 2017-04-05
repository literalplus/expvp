/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.misc;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Manages registration of initialisation (e.g. join) and deinitialisation (e.g. leave) tasks for players.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-05
 */
public interface PlayerInitService {
    /**
     * Calls all registered init handlers.
     *
     * @param player the player to (re-)initialise
     */
    void callInitHandlers(Player player);

    /**
     * Registers an initialisation handler.
     *
     * @param handler the handler to register
     */
    void registerInitHandler(Consumer<Player> handler);

    /**
     * Calls all registered de-init handlers.
     *
     * @param player the player to deinitialise
     */
    void callDeInitHandlers(Player player);

    /**
     * Registers a deinitialisation handler.
     *
     * @param handler the handler to register
     */
    void registerDeInitHandler(Consumer<Player> handler);
}
