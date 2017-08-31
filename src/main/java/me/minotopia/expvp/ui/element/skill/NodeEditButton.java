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

package me.minotopia.expvp.ui.element.skill;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import me.minotopia.expvp.ui.menu.EditNodeMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A button for editing the skill of a node itself.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-19
 */
public class NodeEditButton extends EditButton {
    private final DisplayNameService names;

    public NodeEditButton(SimpleSkillTreeNode node, EditNodeMenu menu, DisplayNameService names) {
        super(menu, node);
        this.names = names;
    }

    @Override
    protected ItemStack createTemplate() {
        return new ItemStackFactory(Material.BOOK_AND_QUILL)
                .displayName(localize("admin!ui.skilledit.item-name"))
                .lore(localize("admin!ui.skilledit.change-add-lore", names.displayName(getNode().getValue())))
                .produce();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        getMenu().openSelectSkillMenu(skill -> {
            getNode().setValue(skill);
            I18n.sendLoc(menu.getPlayer(), Format.success("admin!ui.skilledit.success"));
            getMenu().saveTree();
            menu.open();
        });
    }
}
