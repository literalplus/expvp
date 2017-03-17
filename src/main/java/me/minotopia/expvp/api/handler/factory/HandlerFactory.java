/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.factory;

import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * A factory for skill handlers that creates handlers defined by formatted strings, so-called
 * handler specifications, or handler specs for short.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public interface HandlerFactory extends HandlerSpecNode {
    /**
     * @return a human-readable string describing what handlers this factory supports and the
     * individual handler spec format for each of them
     */
    String getDescription();

    /**
     * Creates a new handler by given handler spec.
     *
     * @param skill       the skill the handler is going to handle
     * @return the created handler
     */
    SkillHandler createHandler(Skill skill) throws InvalidHandlerSpecException;
}
