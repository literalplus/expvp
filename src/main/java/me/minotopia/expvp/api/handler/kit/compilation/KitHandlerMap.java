/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit.compilation;


import com.google.common.collect.Multimap;
import me.minotopia.expvp.api.handler.HandlerMap;
import me.minotopia.expvp.api.handler.kit.KitHandler;
import me.minotopia.expvp.api.handler.kit.KitSlot;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;

/**
 * A handler map that stores {@link KitHandler}s.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-17
 */
public interface KitHandlerMap extends HandlerMap<KitHandler> {
    /**
     * Gets the mapping of kit item type to relevant handlers that are registered to this handler.
     * The map will be filtered such that it will only contain handlers handling one of the given
     * skills.
     *
     * @param skills the collection of skills to filter by
     * @return the collection of relevant handlers for given arguments
     */
    Multimap<KitSlot, KitHandler> getTypeHandlerMap(Collection<? extends Skill> skills);
}
