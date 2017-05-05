/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.misc;

import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-05
 */
public class InventoryRepairService implements RepairService {
    private final Set<Material> repairable = Sets.newHashSet(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.WOOD_SWORD, Material.BOW, Material.WOOD_AXE, Material.WOOD_SPADE
    );

    @Override
    public void repair(Player player) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && repairable.contains(item.getType())) {
                item.setDurability(item.getType().getMaxDurability());
                player.getInventory().setItem(i, item);
            }
        }
    }
}
