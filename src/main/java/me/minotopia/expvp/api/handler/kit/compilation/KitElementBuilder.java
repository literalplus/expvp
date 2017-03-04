/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit.compilation;

import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.handler.kit.KitSlot;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

/**
 * Mutable builder for {@link KitElement}s.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
public interface KitElementBuilder {
    /**
     * Adds an integer to the amount of the resulting element. If the amount is currently zero,
     * meaning that the item would not be added to the kit, and the given integer is also zero, the
     * amount is set to one. This allows for not adding to the amount if the item is already marked
     * for inclusion, but making sure it's getting included.
     *
     * @param toAdd the integer to add the the amount
     * @return this builder
     */
    KitElementBuilder addToAmount(int toAdd);

    /**
     * Makes sure the resulting element will be included in the compiled kit by setting its amount
     * to a non-zero value if it is not already greater than zero.
     *
     * @return this builder
     */
    KitElementBuilder include();

    /**
     * Adds an enchantment to this builder. Note that enchantments are {@link
     * ItemStack#addUnsafeEnchantment(Enchantment, int) not checked for applicability} to the target
     * item type and the level is not bound-checked in any way. There are also no checks for
     * conflicting enchantments. If given enchantment is already present with a lower level, it will
     * be replaced. If given enchantment is already present with an  equal or higher level, it will
     * not be modified.
     *
     * @param enchantment the enchantment to add to the resulting element
     * @param level       the level of the enchantment to add
     * @return this builder
     */
    KitElementBuilder withEnchantment(Enchantment enchantment, int level);

    /**
     * Causes this builder to build a potion instead of the type of item specified by the {@link
     * KitSlot item type}. Uses given potion type and level. If there is already a different
     * potion type set on this item, it will be overridden. If given type is already set on this
     * builder with an equal or higher level, it will not be modified. If it has a lower level, it
     * will be overridden.
     *
     * @param type  the type of potion to set
     * @param level the level of the potion
     * @return this builder
     */
    KitElementBuilder asPotion(PotionType type, int level);

    /**
     * Gets the underlying item stack factory for modification of the raw stack that is being built.
     * Note that modifying the raw stack circumvents any conflict detection and changes may be
     * overridden by other handlers. Additionally, all changes might be discarded if the material of
     * the generated element is changed, for example by {@link #asPotion(PotionType, int) changing
     * the element to a potion}.
     *
     * @return an item stack factory for changing the underlying item stack
     */
    ItemStackFactory factory();

    /**
     * Builds a kit element from the current state of this builder.
     *
     * @return the built element
     */
    KitElement build();
}
