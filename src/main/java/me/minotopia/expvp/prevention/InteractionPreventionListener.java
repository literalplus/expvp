/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.prevention;

import com.google.inject.Inject;
import li.l1t.common.inventory.gui.InventoryMenu;
import me.minotopia.expvp.Permission;
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
        if (Permission.ADMIN_OVERRIDE.has(event.getPlayer())) {
            I18n.sendLoc(event.getPlayer(), "admin!ignore.inv");
            return;
        }
        Inventory inventory = event.getInventory();
        if (isNotAnInventoryMenu(inventory) && isNotAPlayerInventory(inventory)) {
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
