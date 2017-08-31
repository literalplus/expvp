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
import li.l1t.common.inventory.gui.element.MenuElement;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;

/**
 * Abstract base class for menu elements that store their own instances of skill tree nodes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-18
 */
abstract class AbstractNodeElement<M extends InventoryMenu> implements MenuElement {
    private final M menu;
    protected final SimpleSkillTreeNode node;

    AbstractNodeElement(M menu, SimpleSkillTreeNode node) {
        this.menu = menu;
        this.node = node;
    }

    public SimpleSkillTreeNode getNode() {
        return node;
    }

    protected M getMenu() {
        return menu;
    }

    protected String localize(String key, Object... arguments) {
        return I18n.loc(getMenu().getPlayer(), key, arguments);
    }
}
