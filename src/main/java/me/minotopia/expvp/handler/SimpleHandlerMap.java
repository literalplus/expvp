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

package me.minotopia.expvp.handler;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.handler.HandlerMap;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A simple implementation of a handler map.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
@Singleton
public class SimpleHandlerMap implements HandlerMap {
    private final Map<Skill, SkillHandler> handlers = new HashMap<>();
    private final EPPlugin plugin;

    @Inject
    public SimpleHandlerMap(EPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerHandler(SkillHandler handler) {
        Preconditions.checkNotNull(handler, "handler");
        Preconditions.checkNotNull(handler.getSkill(), "handler.getSkill()");
        handlers.put(handler.getSkill(), handler);
        handler.enable(plugin);
    }

    @Override
    public void unregisterHandler(SkillHandler handler) {
        Preconditions.checkNotNull(handler, "handler");
        Preconditions.checkNotNull(handler.getSkill(), "handler.getSkill()");
        handlers.remove(handler.getSkill(), handler);
        handler.disable(plugin);
    }

    @Override
    public void unregisterHandler(Skill skill) {
        Preconditions.checkNotNull(skill, "skill");
        findHandler(skill).ifPresent(this::unregisterHandler);
    }

    @Override
    public Optional<SkillHandler> findHandler(Skill skill) {
        return Optional.ofNullable(handlers.get(skill));
    }

    @Override
    public Collection<SkillHandler> getAllHandlers() {
        return Collections.unmodifiableCollection(handlers.values());
    }

    @Override
    public Collection<SkillHandler> getRelevantHandlers(Collection<Skill> skills) {
        Preconditions.checkNotNull(skills, "skills");
        return skills.stream()
                .map(this::findHandler)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
