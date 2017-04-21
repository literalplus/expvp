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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A button that links to the /stats top command.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
public class StatsTopLinkButton implements MenuElement {
    private final ItemStack stack;

    public StatsTopLinkButton(Player player) {
        stack = new ItemStackFactory(Material.DIAMOND_SWORD)
                .displayName(I18n.loc(player, "core!inv.main.stats-top-link"))
                .produce();
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        return stack;
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        menu.getPlayer().chat("/stats top");
    }
}
