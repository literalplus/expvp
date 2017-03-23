/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory.damage;

import me.minotopia.expvp.handler.damage.VictimHealHandler;
import me.minotopia.expvp.handler.factory.HandlerArgs;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * Creates victim heal handlers.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class VictimHealHandlerFactory extends AbstractDamageHandlerFactory {
    public VictimHealHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    @Override
    public String getDescription() {
        return "victim heal handler: probability,health_points_healed (not hearts!)";
    }

    @Override
    protected VictimHealHandler createHandler(Skill skill, HandlerArgs args) {
        return new VictimHealHandler(
                skill, probabilityPerCent(args), args.doubleArg(1)
        );
    }
}
