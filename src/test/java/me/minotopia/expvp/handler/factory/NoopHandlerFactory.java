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
import me.minotopia.expvp.api.handler.factory.HandlerFactory;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;
import me.minotopia.expvp.api.handler.factory.InvalidHandlerSpecException;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * A factory for skill handlers that do nothing.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
class NoopHandlerFactory extends MultiHandlerSpecNode<HandlerFactory, SkillHandler> {
    NoopHandlerFactory(HandlerSpecNode parent, String ownHandlerSpec) {
        super(parent, ownHandlerSpec);
    }

    @Override
    public String getDescription() {
        return "creates noop skill handlers at '" + getHandlerSpec() + "'";
    }

    @Override
    public SkillHandler createHandler(EPPlugin plugin, Skill skill) throws InvalidHandlerSpecException {
        return new NoopSkillHandler(skill);
    }
}
