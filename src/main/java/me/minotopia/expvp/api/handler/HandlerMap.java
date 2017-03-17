/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler;

import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;
import java.util.Optional;

/**
 * Keeps track of available handlers by skill.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
public interface HandlerMap {
    /**
     * Registers a handler with this map and calls its enable method.
     *
     * @param handler the handler to register
     */
    void registerHandler(SkillHandler handler);

    /**
     * Unregisters a handler from this map and calls its disable method.
     *
     * @param handler the handler to unregister
     */
    void unregisterHandler(SkillHandler handler);

    /**
     * Unregisters the handler associated with given skill, if any, and calls its disable method.
     *
     * @param skill the skill whose handlers to unregister
     */
    void unregisterHandler(Skill skill);

    /**
     * @param skill the skill to find the registered handler for
     * @return an optional containing the respective handler, or an empty optional
     */
    Optional<SkillHandler> findHandler(Skill skill);

    /**
     * @return all handlers currently registered with this map
     */
    Collection<SkillHandler> getAllHandlers();

    /**
     * Gets all handlers registered with this map which belong to any of the given skills.
     *
     * @param skills the skills to filter by
     * @return the collection of handlers relevant to given skills
     */
    Collection<SkillHandler> getRelevantHandlers(Collection<? extends Skill> skills);
}
