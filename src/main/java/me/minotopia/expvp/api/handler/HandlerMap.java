/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
    Collection<SkillHandler> getRelevantHandlers(Collection<Skill> skills);
}
