/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
