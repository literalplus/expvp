/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit.compilation;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Immutable element of a kit, representing the compiled state of a single item.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-17
 */
public interface KitElement {

    /**
     * Bakes this element into an item stack.
     *
     * @return the baked item stack
     */
    ItemStack toItemStack();

    /**
     * @return the amount of items this element has
     */
    int getAmount();

    /**
     * @return the collection of enchantments the element has or an empty collection for none
     */
    Collection<Enchantment> getEnchantments();
}
