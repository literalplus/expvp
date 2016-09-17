/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit;

import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.Nameable;
import org.bukkit.Material;

/**
 * A mutable type of item that may be added to a kit by kit handlers.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-17
 */
public interface KitItemType extends Nameable {
    /**
     * @return the id of the slot in the player inventory this item will be placed in by default
     */
    int getDefaultSlot();

    /**
     * @param newDefaultSlot the new id of the slot in the player inventory this item will be placed
     *                       in by default
     */
    void setDefaultSlot(int newDefaultSlot);

    /**
     * @return the material of the item represented by this type
     */
    Material getMaterial();

    /**
     * @param newType the new material of the item represented by this type
     */
    void setMaterial(Material newType);

    /**
     * Creates a template for item stacks of this type.
     *
     * @return a factory for stacks of this type
     */
    ItemStackFactory createBaseStack();
}
