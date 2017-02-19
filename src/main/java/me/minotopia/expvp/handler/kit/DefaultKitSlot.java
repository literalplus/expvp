/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.kit;

import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.handler.kit.KitSlot;
import org.bukkit.Material;

/**
 * The default kit slots provided by Expvp.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-02-19
 */
public enum DefaultKitSlot implements KitSlot {
    SWORD(0, Material.WOOD_SWORD),
    GOLDEN_APPLES(1, Material.GOLDEN_APPLE),
    BOW(2, Material.BOW),
    ENDER_PEARLS(3, Material.ENDER_PEARL),
    ENCHANTED_GOLDEN_APPLE(4, Material.GOLDEN_APPLE),
    POTION_FIRE_RESISTANCE(6, Material.POTION),
    POTION_SPEED(7, Material.POTION),
    POTION_REGENERATION(8, Material.POTION),
    ARMOR_HELMET(103, Material.LEATHER_HELMET),
    ARMOR_CHESTPLATE(102, Material.LEATHER_CHESTPLATE),
    ARMOR_LEGGINGS(101, Material.LEATHER_LEGGINGS),
    ARMOR_BOOTS(100, Material.LEATHER_BOOTS);

    private final int minecraftSlotId;
    private final Material material;

    DefaultKitSlot(int minecraftSlotId, Material material) {
        this.minecraftSlotId = minecraftSlotId;
        this.material = material;
    }

    @Override
    public int getMinecraftSlotId() {
        return minecraftSlotId;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public ItemStackFactory createBaseStack() {
        return new ItemStackFactory(getMaterial());
    }
}
