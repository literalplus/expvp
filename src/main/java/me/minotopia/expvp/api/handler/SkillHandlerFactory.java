/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler;

import me.minotopia.expvp.EPPlugin;

/**
 * A factory for skill handlers that creates handlers defined by formatted strings, so-called
 * handler specifications, or handler specs for short.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-14
 */
public interface SkillHandlerFactory extends HandlerSpecNode {
    /**
     * @return a human-readable string describing what handlers this factory supports and the
     * individual handler spec format for each of them
     */
    String getDescription();

    /**
     * Creates a new handler by given handler spec.
     *
     * @param plugin      the plugin creating the handler
     * @param handlerSpec the handler spec to create a handler for
     * @return the created handler
     */
    SkillHandler createHandler(EPPlugin plugin, String handlerSpec) throws InvalidHandlerSpecException;
}
