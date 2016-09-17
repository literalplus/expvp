/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit.compilation;

import org.bukkit.potion.PotionType;

/**
 * A kit element that has potion metadata associated with it.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-17
 */
public interface PotionKitElement extends KitElement {
    /**
     * @return the type of potion this element represents
     */
    PotionType getPotionType();

    /**
     * @return the level of the potion that this element represents
     */
    int getLevel();
}
