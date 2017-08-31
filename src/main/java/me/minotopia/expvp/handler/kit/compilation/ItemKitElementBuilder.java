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
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.handler.kit.compilation.KitElement;
import me.minotopia.expvp.api.handler.kit.compilation.KitElementBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

/**
 * A kit element builder that builds kit elements based on item stacks.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public class ItemKitElementBuilder implements KitElementBuilder {
    private final int slotId;
    private final Material type;
    private ItemStackFactory factory;
    private PotionType potionType;
    private int potionLevel;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private boolean splashPotion = false;

    public ItemKitElementBuilder(Material type, int slotId) {
        this.slotId = slotId;
        this.type = type;
    }

    @Override
    public KitElementBuilder addToAmount(int toAdd) {
        Preconditions.checkArgument(toAdd >= 0, "toAdd must be non-negative");
        if (toAdd > 0) {
            factory().amount(factory().getBase().getAmount() + toAdd);
        } else {
            include();
        }
        return this;
    }

    @Override
    public KitElementBuilder include() {
        factory();
        return this;
    }

    @Override
    public KitElementBuilder withEnchantment(Enchantment enchantment, int newLevel) {
        Preconditions.checkNotNull(enchantment, "enchantment");
        this.enchantments.merge(enchantment, newLevel, Math::max);
        return this;
    }

    @Override
    public KitElementBuilder asPotion(PotionType type, int level) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkArgument(level > 0 && level <= type.getMaxLevel(),
                "potion level out of bounds: %s %s", type, level);
        include();
        if (this.potionType != type || level > this.potionLevel) {
            this.potionType = type;
            this.potionLevel = level;
        }
        return this;
    }

    @Override
    public KitElementBuilder asSplashPotion() {
        this.splashPotion = true;
        return this;
    }

    @Override
    public ItemStackFactory factory() {
        if (factory == null) {
            factory = new ItemStackFactory(type);
        }
        return factory;
    }

    @Override
    public KitElement build() {
        enchantments.forEach((ench, level) -> factory().enchantUnsafe(ench, level));
        if (potionType != null) {
            return new PotionKitElement(factory().produce(), potionType, potionLevel, splashPotion);
        } else {
            return new SimpleKitElement(factory().produce());
        }
    }

    @Override
    public boolean isIncluded() {
        return factory != null;
    }

    @Override
    public int getSlotId() {
        return slotId;
    }

    @Override
    public String toString() {
        return "ItemKitElementBuilder{" +
                "slotId=" + slotId +
                ", type=" + type +
                ", potionType=" + potionType +
                ", potionLevel=" + potionLevel +
                ", enchantments=" + enchantments +
                '}';
    }
}
