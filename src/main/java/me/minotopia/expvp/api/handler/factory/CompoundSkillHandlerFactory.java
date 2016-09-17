/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.factory;

import java.util.Collection;

/**
 * A skill handler factory that splits handler specs in parts and delegates handler creation to its
 * children based on these parts.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-14
 */
public interface CompoundSkillHandlerFactory extends SkillHandlerFactory {
    /**
     * Registers a handler factory as a child of this compound factory.
     *
     * @param child the child to register
     */
    void addChild(SkillHandlerFactory child);

    /**
     * @return the collection of this factory's children
     */
    Collection<SkillHandlerFactory> getChildren();
}
