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

import li.l1t.common.inventory.gui.holder.ElementHolder;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import me.minotopia.expvp.ui.menu.EditNodeMenu;
import org.bukkit.inventory.ItemStack;

/**
 * Abstract base classes for buttons that allow to edit skill to node assignments.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-19
 */
abstract class EditButton extends AbstractNodeElement<EditNodeMenu> {
    private ItemStack template = null;

    EditButton(EditNodeMenu menu, SimpleSkillTreeNode node) {
        super(menu, node);
    }

    protected abstract ItemStack createTemplate();

    @Override
    public ItemStack draw(ElementHolder holder) {
        if (template == null) {
            template = createTemplate();
        }
        return template.clone();
    }
}
