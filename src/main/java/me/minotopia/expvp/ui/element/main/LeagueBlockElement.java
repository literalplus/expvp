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
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A menu element that shows information about a league.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-20
 */
public class LeagueBlockElement implements MenuElement {
    private final ItemStack stack;

    public LeagueBlockElement(Player player, League league, DisplayNameService names) {
        this.stack = new ItemStackFactory(league.getDisplayMaterial())
                .displayName(I18n.loc(player, "core!inv.main.league-item"))
                .lore(I18n.loc(player, names.displayName(league)))
                .produce();
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        return stack.clone();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        //no-op
    }
}
