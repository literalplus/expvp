/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit;


import com.google.common.collect.Multimap;
import me.minotopia.expvp.api.handler.HandlerMap;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;

/**
 * A handler map that stores {@link KitHandler}s.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
public interface KitHandlerMap extends HandlerMap<KitHandler>, HandlerSpecNode {
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
