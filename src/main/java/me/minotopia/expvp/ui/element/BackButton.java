/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.element;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.element.Placeholder;
import li.l1t.common.util.inventory.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * A button that restores the previous menu, if any, on click. If no previous menu exists, it closes
 * the current inventory.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-15
 */
public class BackButton extends Placeholder {
    private static final ItemStackFactory ICON_FACTORY = new ItemStackFactory(Material.WOOD_DOOR)
            .displayName("<< §6§lZurück");
    private final InventoryMenu previous;

    public BackButton(InventoryMenu previous) {
        super(ICON_FACTORY.produce());
        this.previous = previous;
    }

    @Override
    public void handleMenuClick(InventoryClickEvent event, InventoryMenu inventoryMenu) {
        if (previous != null) {
            previous.open();
        } else {
            inventoryMenu.getPlayer().closeInventory();
        }
    }
}
