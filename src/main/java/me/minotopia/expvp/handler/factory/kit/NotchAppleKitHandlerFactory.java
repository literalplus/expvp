/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory.kit;

import me.minotopia.expvp.handler.factory.HandlerArgs;
import me.minotopia.expvp.handler.kit.NotchAppleKitHandler;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * A factory for notch apple kit handlers with just amount.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-16
 */
public class NotchAppleKitHandlerFactory extends AbstractKitHandlerFactory {
    public NotchAppleKitHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    @Override
    public String getDescription() {
        return "notch apple kit handler: slot,amount";
    }

    @Override
    protected NotchAppleKitHandler createHandler(Skill skill, HandlerArgs args) {
        return new NotchAppleKitHandler(skill, slotId(args), args.intArg(AMOUNT_INDEX - 1));
    }

}
