/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory.damage;

import me.minotopia.expvp.handler.damage.PoisonousArmorHandler;
import me.minotopia.expvp.handler.factory.HandlerArgs;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * Creates poisonous armor handlers.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class PoisonousArmorHandlerFactory extends AbstractDamageHandlerFactory {
    public PoisonousArmorHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    @Override
    public String getDescription() {
        return "poisonous armor: probability, duration_seconds";
    }

    @Override
    protected PoisonousArmorHandler createHandler(Skill skill, HandlerArgs args) {
        return new PoisonousArmorHandler(
                skill, probabilityPerCent(args), args.intArg(1)
        );
    }
}
