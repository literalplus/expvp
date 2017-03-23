/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.factory;

import me.minotopia.expvp.api.handler.damage.DamageHandler;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * A  handler factory that creates damage handlers.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public interface DamageHandlerFactory extends HandlerFactory {
    @Override
    DamageHandler createHandler(Skill skill) throws InvalidHandlerSpecException;
}
