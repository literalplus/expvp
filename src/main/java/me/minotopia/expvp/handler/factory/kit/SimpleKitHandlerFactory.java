/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory.kit;

import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.handler.kit.SimpleKitHandler;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * A factory for simple kit handlers with amount and material.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public class SimpleKitHandlerFactory extends AbstractKitHandlerFactory<SimpleKitHandler> {
    public SimpleKitHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    @Override
    public String getDescription() {
        return "simple kit handler: slot,material,amount";
    }

    @Override
    protected SimpleKitHandler createHandler(EPPlugin plugin, Skill skill, KitArgs args) {
        return new SimpleKitHandler(skill, slotId(args), material(args), amount(args));
    }

}
