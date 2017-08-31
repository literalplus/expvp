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
    public PotionKitElement(ItemStack stack, PotionType type, int level, boolean splashPotion) {
        super(applyPotion(stack, type, level, splashPotion));
    }

    private static ItemStack applyPotion(ItemStack stack, PotionType type, int level, boolean splashPotion) {
        Potion potion = new Potion(type, level);
        if (splashPotion) {
            potion.splash();
        }
        potion.apply(stack);
        return stack;
    }
}
