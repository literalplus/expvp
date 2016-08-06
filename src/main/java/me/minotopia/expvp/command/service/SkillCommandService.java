/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.service;

import li.l1t.common.intake.CommandsManager;
import li.l1t.common.intake.exception.CommandArgumentException;
import me.minotopia.expvp.command.provider.SkillProvider;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skill.meta.SkillManager;

import java.io.IOException;

/**
 * Provides commonly used utilities for commands working with skills.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-06
 */
public class SkillCommandService {
    private final SkillManager skillManager;

    public SkillCommandService(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    public void saveSkill(Skill skill) {
        try {
            skillManager.save(skill);
        } catch (IOException e) {
            throw new IllegalStateException("Konnte Skill nicht speichern!", e);
        }
    }

    public Skill createSkillWithExistsCheck(String id) throws IOException {
        assureThereIsNoSkillWithId(id);
        return skillManager.create(id);
    }

    public void assureThereIsNoSkillWithId(String id) {
        if (skillManager.contains(id)) {
            throw new CommandArgumentException(String.format(
                    "Es gibt bereits einen Skill mit der ID '%s'!",
                    id));
        }
    }

    public Skill getSkillOrFail(String id) {
        Skill skill = skillManager.get(id);
        if (skill == null) {
            throw new CommandArgumentException(String.format(
                    "Es gibt keinen Skill mit der ID '%s'!",
                    id));
        }
        return skill;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public static SkillCommandService registerSkillInjections(CommandsManager commandsManager,
                                                              SkillManager skillManager) {
        SkillCommandService instance = new SkillCommandService(skillManager);
        commandsManager.bind(SkillManager.class).toInstance(skillManager);
        commandsManager.bind(SkillCommandService.class).toInstance(instance);
        commandsManager.bind(Skill.class).toProvider(new SkillProvider(instance));
        return instance;
    }
}
