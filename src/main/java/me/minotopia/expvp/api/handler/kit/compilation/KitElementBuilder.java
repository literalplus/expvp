/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.minotopia.expvp.api.handler.kit.compilation;

import li.l1t.common.util.inventory.ItemStackFactory;
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
     * Adds an integer to the amount of the resulting element. If given integer is zero, this method only makes sure
     * that this element will be {@link #include() included in the kit}. This allows for not adding to the amount if the
     * item is already marked for inclusion, but making sure it's getting included.
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
     * Causes this builder to build a potion. Uses given potion type and level. If there is already a different
     * potion type set on this item, it will be overridden. If given type is already set on this
     * builder with an equal or higher level, it will not be modified. If it has a lower level, it
     * will be overridden.
     *
     * @param type  the type of potion to set
     * @param level the level of the potion
     * @return this builder
     */
    KitElementBuilder asPotion(PotionType type, int level);

    KitElementBuilder asSplashPotion();

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

    /**
     * @return whether the item represented by this builder is supposed to be included in the kit
     */
    boolean isIncluded();

    /**
     * @return the integer id of the slot this element is in, as used by Minecraft
     */
    int getSlotId();
}
