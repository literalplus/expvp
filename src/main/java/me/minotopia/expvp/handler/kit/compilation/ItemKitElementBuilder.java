/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.kit.compilation;

import com.google.common.base.Preconditions;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.handler.kit.compilation.KitElement;
import me.minotopia.expvp.api.handler.kit.compilation.KitElementBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

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

    public ItemKitElementBuilder(Material type, int slotId) {
        this.slotId = slotId;
        this.type = type;
    }

    @Override
    public KitElementBuilder addToAmount(int toAdd) {
        Preconditions.checkArgument(toAdd > 0, "toAdd must be positive");
        factory().amount(factory().getBase().getAmount() + toAdd);
        return this;
    }

    @Override
    public KitElementBuilder include() {
        factory();
        return this;
    }

    @Override
    public KitElementBuilder withEnchantment(Enchantment enchantment, int level) {
        factory().enchantUnsafe(enchantment, level);
        return this;
    }

    @Override
    public KitElementBuilder asPotion(PotionType type, int level) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkArgument(level > 0 && level <= potionType.getMaxLevel(),
                "potion level out of bounds: %s %s", type, level);
        factory();
        if (this.potionType != type || level > this.potionLevel) {
            this.potionType = type;
            this.potionLevel = level;
        }
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
        if (potionType != null) {
            return new PotionKitElement(factory().produce(), potionType, potionLevel);
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
}
