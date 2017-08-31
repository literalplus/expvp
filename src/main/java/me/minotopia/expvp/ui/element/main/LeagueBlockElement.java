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
