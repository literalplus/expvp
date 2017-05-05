/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.misc;

import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-05
 */
public class InventoryRepairService implements RepairService {
    private final Logger LOGGER = LoggingManager.getLogger(InventoryRepairService.class);
    @Override
    public void repair(Player player) {
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && isRepairable(item.getType())) {
                item.setDurability(item.getType().getMaxDurability());
                inv.setItem(i, item);
                LOGGER.debug("Repairing {} - {} to {}", player.getName(), item.getType(), item.getDurability());
            }
        }
    }

    private boolean isRepairable(Material material) {
        switch (material) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
            case WOOD_SWORD:
            case WOOD_SPADE:
            case WOOD_AXE:
            case BOW:
                return true;
            default:
                return false;
        }
    }
}
