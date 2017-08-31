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

package me.minotopia.expvp.api.misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-05
 */
public class InventoryRepairService implements RepairService {

    @Override
    public void repair(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.setContents(repairAll(inv.getContents()));
        inv.setArmorContents(repairAll(inv.getArmorContents()));
        player.updateInventory();
    }

    private ItemStack[] repairAll(ItemStack[] contents) {
        for (ItemStack item : contents) {
            if (item != null && isRepairable(item.getType())) {
                item.setDurability((short) 0);
            }
        }
        return contents;
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
