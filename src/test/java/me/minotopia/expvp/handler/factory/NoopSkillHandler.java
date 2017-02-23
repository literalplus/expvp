/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory;

import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * A skill handler that does nothing.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
class NoopSkillHandler implements SkillHandler {
    private final Skill skill;

    NoopSkillHandler(Skill skill) {
        this.skill = skill;
    }

    @Override
    public void enable(EPPlugin plugin) {

    }

    @Override
    public void disable(EPPlugin plugin) {

    }

    @Override
    public Skill getSkill() {
        return skill;
    }
}
