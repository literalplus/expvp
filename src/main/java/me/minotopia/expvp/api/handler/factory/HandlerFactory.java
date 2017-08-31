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
