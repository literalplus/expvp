/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.kit.compilation;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

/**
 * A kit element with a potion effect.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public class PotionKitElement extends SimpleKitElement {
    public PotionKitElement(ItemStack stack, PotionType type, int level) {
        super(applyPotion(stack, type, level));
    }

    private static ItemStack applyPotion(ItemStack stack, PotionType type, int level) {
        new Potion(type, level).apply(stack);
        return stack;
    }
}
