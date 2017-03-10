/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * Base class for handlers. Override methods to change the default no-op behaviour.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public class HandlerAdapter implements SkillHandler {
    private final Skill skill;

    public HandlerAdapter(Skill skill) {
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
