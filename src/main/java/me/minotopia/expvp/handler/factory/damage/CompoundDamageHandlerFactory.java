/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory.damage;

import me.minotopia.expvp.api.handler.damage.DamageHandler;
import me.minotopia.expvp.api.handler.factory.DamageHandlerFactory;
import me.minotopia.expvp.handler.factory.MapCompoundHandlerFactory;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * A compound handler factory for damage handlers.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class CompoundDamageHandlerFactory extends MapCompoundHandlerFactory<DamageHandlerFactory>
        implements DamageHandlerFactory {
    public CompoundDamageHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    @Override
    public DamageHandler createHandler(Skill skill) {
        return (DamageHandler) super.createHandler(skill);
    }
}
