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
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * A menu element that shows a player's head.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-20
 */
public class PlayerHeadElement implements MenuElement {
    private final String targetName;
    private final UUID senderId;

    public PlayerHeadElement(Player player, String targetName) {
        this.targetName = targetName;
        this.senderId = player.getUniqueId();
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        ItemStackFactory factory = new ItemStackFactory(Material.SKULL_ITEM);
        if (targetName != null) {
            factory.displayName(targetName).skullOwner(targetName);
        } else {
            factory.displayName("§c???").lore(I18n.loc(senderId, "core!inv.main.no-friend"));
        }
        return factory.produce();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        //TODO: https://bugs.l1t.li/view.php?id=794
    }
}
