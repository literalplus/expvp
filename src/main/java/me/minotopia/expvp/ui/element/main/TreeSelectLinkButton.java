/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.element.main;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.ui.menu.SelectTreeMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A menu button that links to the tree selection menu.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
public class TreeSelectLinkButton implements MenuElement {
    private final SelectTreeMenu.Factory selectTreeMenuFactory;
    private final ItemStack stack;

    public TreeSelectLinkButton(Player player, SelectTreeMenu.Factory selectTreeMenuFactory) {
        this.selectTreeMenuFactory = selectTreeMenuFactory;
        this.stack = new ItemStackFactory(Material.BREWING_STAND_ITEM)
                .displayName(I18n.loc(player, "core!tree-select.title"))
                .produce();
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        return stack;
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        selectTreeMenuFactory.openForResearch(menu.getPlayer());
    }
}
