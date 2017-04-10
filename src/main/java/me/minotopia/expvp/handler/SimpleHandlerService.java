/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.handler.HandlerFactoryGraph;
import me.minotopia.expvp.api.handler.HandlerMap;
import me.minotopia.expvp.api.handler.HandlerService;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.api.skill.SkillService;
import me.minotopia.expvp.logging.LoggingManager;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.util.SessionProvider;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static li.l1t.common.util.PredicateHelper.not;

/**
 * Simple implementation of a handler service.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
@Singleton
public class SimpleHandlerService implements HandlerService {
    private final Logger LOGGER = LoggingManager.getLogger(SimpleHandlerService.class);
    private final HandlerMap handlerMap;
    private final HandlerFactoryGraph factories;
    private final SkillService skillService;
    private final Multimap<Skill, UUID> skillRequirementsMap = MultimapBuilder.hashKeys().arrayListValues().build();

    @Inject
    public SimpleHandlerService(HandlerMap handlerMap, HandlerFactoryGraph factories, SkillService skillService,
                                PlayerInitService initService, PlayerDataService players, SessionProvider sessionProvider) {
        this.handlerMap = handlerMap;
        this.factories = factories;
        this.skillService = skillService;
        initService.registerInitHandler(player ->
                sessionProvider.inSession(
                        ignored -> players.findData(player.getUniqueId()).ifPresent(this::registerHandlers)
                ));
        initService.registerDeInitHandler(player -> skillRequirementsMap.values().removeIf(player.getUniqueId()::equals));
    }

    @Override
    public void registerHandlers(PlayerData playerData) {
        skillService.getSkills(playerData)
                .forEach(skill -> {
                    skillRequirementsMap.put(skill, playerData.getUniqueId());
                    handlerMap.findHandler(skill).map(Optional::of)
                            .orElseGet(() -> createAndRegisterHandlerFor(skill));
                });
    }

    @Override
    public void unregisterHandlers(PlayerData playerData) {
        skillRequirementsMap.values().removeIf(playerData.getUniqueId()::equals);
        purgeStaleHandlers();
    }

    private void purgeStaleHandlers() {
        ImmutableList.copyOf(handlerMap.getAllHandlers()).stream()
                .map(SkillHandler::getSkill)
                .filter(not(skillRequirementsMap::containsKey))
                .forEach(handlerMap::unregisterHandler);
    }

    private Optional<SkillHandler> createAndRegisterHandlerFor(Skill skill) {
        Preconditions.checkNotNull(skill, "skill");
        if (skill.getHandlerSpec() == null || skill.getHandlerSpec().isEmpty()) {
            return Optional.empty();
        }
        return tryCreateAndRegisterHandlerFor(skill);
    }

    private Optional<SkillHandler> tryCreateAndRegisterHandlerFor(Skill skill) {
        try {
            SkillHandler handler = factories.createHandler(skill);
            handlerMap.registerHandler(handler);
            return Optional.of(handler);
        } catch (Exception e) {
            LOGGER.warn("Failed to resolve handler for skill " + skill, e);
            return Optional.empty();
        }
    }

    @Override
    public <T extends SkillHandler> Stream<T> handlersOfTypeStream(Class<? extends T> type, PlayerData playerData) {
        return handlerMap.getRelevantHandlers(skillService.getSkills(playerData)).stream()
                .filter(handler -> type.isAssignableFrom(handler.getClass()))
                .map(type::cast);
    }
}
