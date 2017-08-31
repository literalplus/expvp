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

package me.minotopia.expvp.prevention;

import com.google.inject.Inject;
import li.l1t.common.inventory.gui.InventoryMenu;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

/**
 * Prevents interactions with the world.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-18
 */
public class InteractionPreventionListener implements Listener {
    @Inject
    private InteractionPreventionListener() {

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onInvOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        if (isNotAnInventoryMenu(inventory) && isNotAPlayerInventory(inventory)) {
            if (Permission.ADMIN_OVERRIDE.has(event.getPlayer())) {
                I18n.sendLoc(event.getPlayer(), Format.success("admin!ignore.inv"));
                return;
            }
            event.setCancelled(true);
        }
    }

    private boolean isNotAnInventoryMenu(Inventory inventory) {
        return !(inventory.getHolder() instanceof InventoryMenu);
    }

    private boolean isNotAPlayerInventory(Inventory inventory) {
        return inventory.getType() != InventoryType.CREATIVE &&
                inventory.getType() != InventoryType.PLAYER;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null) {
            if (block.getType() == Material.TRAP_DOOR ||
                    block.getType() == Material.BED_BLOCK) {
                event.setCancelled(true);
            }
        }
    }
}
