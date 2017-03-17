/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.handler.HandlerMap;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple implementation of a handler map.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
public class SimpleHandlerMap<T extends SkillHandler> implements HandlerMap<T> {
    private final Map<Skill, T> handlers = new HashMap<>();
    private final EPPlugin plugin;

    @Inject
    public SimpleHandlerMap(EPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerHandler(T handler) {
        Preconditions.checkNotNull(handler, "handler");
        Preconditions.checkNotNull(handler.getSkill(), "handler.getSkill()");
        handlers.put(handler.getSkill(), handler);
        handler.enable(plugin);
    }

    @Override
    public void unregisterHandler(T handler) {
        Preconditions.checkNotNull(handler, "handler");
        Preconditions.checkNotNull(handler.getSkill(), "handler.getSkill()");
        handlers.remove(handler.getSkill(), handler);
        handler.disable(plugin);
    }

    @Override
    public void unregisterHandler(Skill skill) {
        Preconditions.checkNotNull(skill, "skill");
        Optional.ofNullable(handlers.get(skill))
                .ifPresent(this::unregisterHandler);
    }

    @Override
    public Collection<T> getAllHandlers() {
        return Collections.unmodifiableCollection(handlers.values());
    }

    @Override
    public Collection<T> getRelevantHandlers(Collection<? extends Skill> skills) {
        Preconditions.checkNotNull(skills, "skills");
        return handlerStream()
                .filter(handler -> skills.contains(handler.getSkill()))
                .collect(Collectors.toList());
    }

    private Stream<T> handlerStream() {
        return handlers.entrySet().stream()
                .map(Map.Entry::getValue);
    }
}
