/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.handler.HandlerMap;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A simple implementation of a handler map.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-17
 */
public class SimpleHandlerMap<T extends SkillHandler> implements HandlerMap<T> {
    private final Collection<T> handlers = new ArrayList<>();

    @Override
    public void registerHandler(T handler) {
        Preconditions.checkNotNull(handler, "handler");
        Preconditions.checkNotNull(handler.getSkill(), "handler.getSkill()");
        handlers.add(handler);
    }

    @Override
    public void unregisterHandler(T handler) {
        Preconditions.checkNotNull(handler, "handler");
        handlers.remove(handler);
    }

    @Override
    public void unregisterHandlers(Skill skill) {
        Preconditions.checkNotNull(skill, "skill");
        handlers.removeIf(handler -> handler.getSkill().equals(skill));
    }

    @Override
    public Collection<T> getAllHandlers() {
        return Collections.unmodifiableCollection(handlers);
    }

    @Override
    public Collection<T> getMatchingHandlers(Predicate<? super T> filter) {
        Preconditions.checkNotNull(filter, "filter");
        return handlers.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<T> getRelevantHandlers(Collection<? extends Skill> skills) {
        Preconditions.checkNotNull(skills, "skills");
        return getMatchingHandlers(handler -> skills.contains(handler.getSkill()));
    }

    @Override
    public Collection<T> getRelevantMatchingHandlers(Collection<? extends Skill> skills, Predicate<? super T> filter) {
        Preconditions.checkNotNull(skills, "skills");
        Preconditions.checkNotNull(filter, "filter");
        return handlers.stream()
                .filter(handler -> skills.contains(handler.getSkill()))
                .filter(filter)
                .collect(Collectors.toList());
    }
}
