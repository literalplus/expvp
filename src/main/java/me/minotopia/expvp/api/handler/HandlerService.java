/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler;

import me.minotopia.expvp.api.model.PlayerData;

import java.util.stream.Stream;

/**
 * Manages the specific handlers for players' skill sets. Dynamically enables handlers when they are requested and
 * disables them when there is no player that needs them.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public interface HandlerService {
    /**
     * Registers all handlers required by given player's skill set and notes that given player requires these skills.
     *
     * @param playerData the player to operate on
     */
    void registerHandlers(PlayerData playerData);

    /**
     * Unregisters handlers required by given player's skill set that are not required by any other player's skill set.
     *
     * @param playerData the player to operate on
     */
    void unregisterHandlers(PlayerData playerData);

    /**
     * Creates a stream of handlers that are instances of given type and included in given player's skill set.
     *
     * @param type       the type of handler to request
     * @param playerData the player whose skill set to check with
     * @param <T>        the type of handler that is returned
     * @return a stream of handlers that are instances of type and in given player's skill set
     */
    <T extends SkillHandler> Stream<T> handlersOfTypeStream(Class<? extends T> type, PlayerData playerData);
}
