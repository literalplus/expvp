/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.handler.InvalidHandlerSpecException;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.api.handler.SkillHandlerFactory;

/**
 * A factory for skill handlers that do nothing.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-14
 */
class NoopSkillHandlerFactory extends AbstractHandlerSpecNode implements SkillHandlerFactory {
    NoopSkillHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    @Override
    public String getDescription() {
        return "creates noop skill handlers at '" + getHandlerSpec() + "'";
    }

    @Override
    public SkillHandler createHandler(EPPlugin plugin, String handlerSpec) throws InvalidHandlerSpecException {
        return new NoopSkillHandler(getHandlerSpec());
    }
}
