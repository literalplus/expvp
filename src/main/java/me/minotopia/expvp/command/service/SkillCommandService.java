/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.service;

import li.l1t.common.intake.CommandsManager;
import me.minotopia.expvp.command.provider.YamlObjectProvider;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skill.meta.SkillManager;

/**
 * Provides commonly used utilities for commands working with skills.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-06
 */
public class SkillCommandService extends YamlManagerCommandService<Skill> {

    public SkillCommandService(SkillManager skillManager) {
        super(skillManager, "Skill");
    }

    public void registerInjections(CommandsManager commandsManager) {
        commandsManager.bind(SkillManager.class).toInstance(getManager());
        commandsManager.bind(SkillCommandService.class).toInstance(this);
        commandsManager.bind(Skill.class).toProvider(new YamlObjectProvider<>(this));
    }

    @Override
    public SkillManager getManager() {
        return (SkillManager) super.getManager();
    }
}
