/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.handler.HandlerMap;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple implementation of a handler map.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
public class SimpleHandlerMap<T extends SkillHandler> implements HandlerMap<T> {
    private final Map<Skill, T> handlers = new HashMap<>();

    @Override
    public void registerHandler(T handler) {
        Preconditions.checkNotNull(handler, "handler");
        Preconditions.checkNotNull(handler.getSkill(), "handler.getSkill()");
        handlers.put(handler.getSkill(), handler);
    }

    @Override
    public void unregisterHandler(T handler) {
        Preconditions.checkNotNull(handler, "handler");
        Preconditions.checkNotNull(handler.getSkill(), "handler.getSkill()");
        handlers.remove(handler.getSkill(), handler);
    }

    @Override
    public void unregisterHandlers(Skill skill) {
        Preconditions.checkNotNull(skill, "skill");
        handlers.remove(skill);
    }

    @Override
    public Collection<T> getAllHandlers() {
        return Collections.unmodifiableCollection(handlers.values());
    }

    @Override
    public Collection<T> getRelevantHandlers(Collection<? extends Skill> skills) {
        Preconditions.checkNotNull(skills, "skills");
        return handlers.entrySet().stream()
                .filter(e -> skills.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
