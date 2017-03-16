/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory.kit;

import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;
import me.minotopia.expvp.api.handler.factory.KitHandlerFactory;
import me.minotopia.expvp.api.handler.kit.KitHandler;
import me.minotopia.expvp.handler.factory.MapCompoundHandlerFactory;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * A compound handler factory for kit handlers.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public class CompoundKitHandlerFactory extends MapCompoundHandlerFactory<KitHandlerFactory>
        implements KitHandlerFactory {
    public CompoundKitHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    public CompoundKitHandlerFactory(HandlerSpecNode parent, String ownHandlerSpec) {
        super(parent, ownHandlerSpec);
    }

    @Override
    public KitHandler createHandler(EPPlugin plugin, Skill skill) {
        return (KitHandler) super.createHandler(plugin, skill);
    }
}
