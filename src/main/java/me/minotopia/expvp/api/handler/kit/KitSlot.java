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
 * Represents a single slot of the Expvp inventory template with a fixed position.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-17
 */
public interface KitSlot extends Nameable {
    /**
     * @return the slot in the player inventory this item will be placed in
     */
    int getMinecraftSlotId();

    /**
     * @return the material of the item represented by this type
     */
    Material getMaterial();

    /**
     * Creates a template for item stacks in this slot.
     *
     * @return a factory for stacks in this slot
     */
    ItemStackFactory createBaseStack();
}
