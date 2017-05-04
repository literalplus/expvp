/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory.damage;

import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.handler.damage.NotTodayHandler;
import me.minotopia.expvp.handler.factory.HandlerArgs;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * Creates not today handlers.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class NotTodayHandlerFactory extends AbstractDamageHandlerFactory {
    private final PlayerInitService initService;

    public NotTodayHandlerFactory(String ownHandlerSpec, PlayerInitService initService) {
        super(ownHandlerSpec);
        this.initService = initService;
    }

    @Override
    public String getDescription() {
        return "poisonous armor: probability, duration_seconds";
    }

    @Override
    protected NotTodayHandler createHandler(Skill skill, HandlerArgs args) {
        return new NotTodayHandler(
                skill, probabilityPerCent(args), args.intArg(1), initService
        );
    }
}
