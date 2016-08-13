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
import me.minotopia.expvp.skill.tree.SkillTree;
import me.minotopia.expvp.skill.tree.SkillTreeManager;

/**
 * Provides commonly used utilities for commands working with skill trees.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-06
 */
public class SkillTreeCommandService extends YamlManagerCommandService<SkillTree> {

    public SkillTreeCommandService(SkillTreeManager manager) {
        super(manager, "Skilltree");
    }

    public void registerInjections(CommandsManager commandsManager) {
        commandsManager.bind(SkillTreeManager.class).toInstance(getManager());
        commandsManager.bind(SkillTreeCommandService.class).toInstance(this);
        commandsManager.bind(SkillTree.class).toProvider(new YamlObjectProvider<>(this));
    }

    @Override
    public SkillTreeManager getManager() {
        return (SkillTreeManager) super.getManager();
    }
}
