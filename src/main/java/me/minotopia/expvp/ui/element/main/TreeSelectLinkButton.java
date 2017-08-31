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
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.ui.menu.SelectTreeMenu;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

    public TreeSelectLinkButton(Player player, PlayerData playerData, SelectTreeMenu.Factory selectTreeMenuFactory) {
        this.selectTreeMenuFactory = selectTreeMenuFactory;
        ItemStackFactory factory = new ItemStackFactory(Material.BREWING_STAND_ITEM)
                .displayName(I18n.loc(player, "core!tree-select.title"));
        if (playerData.getAvailableTalentPoints() > 0) {
            factory.enchantUnsafe(Enchantment.WATER_WORKER, 1).hideEnchants()
                    .amount(playerData.getAvailableTalentPoints());
        }
        this.stack = factory.produce();
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
