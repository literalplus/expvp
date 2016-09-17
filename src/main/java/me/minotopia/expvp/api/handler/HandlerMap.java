/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler;

import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Keeps track of available handlers by skill and type for a single event source.
 *
 * @param <T> the type of handler managed by this map
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-17
 */
public interface HandlerMap<T extends SkillHandler> {
    /**
     * Registers a handler with this map.
     *
     * @param handler the handler to register
     */
    void registerHandler(T handler);

    /**
     * Unregisters a handler from this map.
     *
     * @param handler the handler to unregister
     */
    void unregisterHandler(T handler);

    /**
     * Unregisters all handlers handling given skill.
     *
     * @param skill the skill whose handlers to unregister
     */
    void unregisterHandlers(Skill skill);

    /**
     * @return all handlers currently registered with this map
     */
    Collection<T> getAllHandlers();

    /**
     * Gets all handlers registered with this map which match given predicate.
     *
     * @param filter the predicate for filtering
     * @return the collection of matching handlers
     */
    Collection<T> getMatchingHandlers(Predicate<? super T> filter);

    /**
     * Gets all handlers registered with this map which belong to any of the given skills.
     *
     * @param skills the skills to filter by
     * @return the collection of handlers relevant to given skills
     */
    Collection<T> getRelevantHandlers(Collection<? extends Skill> skills);

    /**
     * Gets all handlers registered with this map which belong to any of the given skills and match
     * given predicate.
     *
     * @param skills the skills to filter by
     * @param filter the predicate to filter by
     * @return the collection of handlers matching given predicate and relevant to given skills
     */
    Collection<T> getRelevantMatchingHandlers(Collection<? extends Skill> skills, Predicate<? super T> filter);
}
