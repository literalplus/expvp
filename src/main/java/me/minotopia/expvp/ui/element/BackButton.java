/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
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

import java.util.function.Consumer;

/**
 * A button that restores the previous menu, if any, on click. If no previous menu exists, it closes
 * the current inventory.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-15
 */
public class BackButton extends Placeholder {
    private static final ItemStackFactory ICON_FACTORY = new ItemStackFactory(Material.WOOD_DOOR)
            .displayName("<< §6§lZurück");
    private final Consumer<InventoryMenu> clickHandler;

    public BackButton(InventoryMenu previous) {
        this(inventoryMenu -> {
            if (previous != null) {
                previous.open();
            } else {
                inventoryMenu.getPlayer().closeInventory();
            }
        });
    }

    public BackButton(Consumer<InventoryMenu> clickHandler) {
        super(ICON_FACTORY.produce());
        this.clickHandler = clickHandler;
    }

    @Override
    public void handleMenuClick(InventoryClickEvent event, InventoryMenu inventoryMenu) {
        clickHandler.accept(inventoryMenu);
    }
}
