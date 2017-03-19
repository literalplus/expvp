/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.kit.compilation;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.handler.kit.compilation.KitElement;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * A simple kit element.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public class SimpleKitElement implements KitElement {
    private final ItemStack stack;

    public SimpleKitElement(ItemStack stack) {
        this.stack = Preconditions.checkNotNull(stack, "stack");
    }

    @Override
    public ItemStack toItemStack() {
        return stack.clone();
    }

    @Override
    public int getAmount() {
        return stack.getAmount();
    }

    @Override
    public Collection<Enchantment> getEnchantments() {
        return stack.getEnchantments().keySet();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " +
                stack.toString();
    }
}
