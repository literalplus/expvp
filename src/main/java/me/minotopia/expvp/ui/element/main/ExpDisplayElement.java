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
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A menu item that displays the current amount of Exp a player has.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-20
 */
public class ExpDisplayElement implements MenuElement {
    private final ItemStack stack;
    private final PlayerData subject;

    public ExpDisplayElement(Player player, PlayerData subject) {
        this.subject = subject;
        this.stack = new ItemStackFactory(Material.EXP_BOTTLE)
                .displayName(I18n.loc(player, "core!inv.main.exp-format", subject.getExp()))
                .lore(I18n.loc(player, "core!inv.main.exp-link"))
                .produce();

    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        return stack.clone();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        menu.getPlayer().chat("/stats " + subject.getUniqueId());
    }
}
