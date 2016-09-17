/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * A skill handler that does nothing.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-14
 */
class NoopSkillHandler extends AbstractHandlerSpecNode implements SkillHandler {
    protected NoopSkillHandler(String ownHandlerSpec) {
        super("sub" + ownHandlerSpec);
    }

    @Override
    public void enable(EPPlugin plugin) {

    }

    @Override
    public void disable(EPPlugin plugin) {

    }

    @Override
    public Skill getSkill() {
        throw new UnsupportedOperationException(); //don't want surprises so let's be fail-fast instead of inventong something
    }
}
