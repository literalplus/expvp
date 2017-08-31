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
