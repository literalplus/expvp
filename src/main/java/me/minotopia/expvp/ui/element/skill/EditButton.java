/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.element.skill;

import li.l1t.common.inventory.gui.InventoryMenu;
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

    EditButton(SimpleSkillTreeNode node) {
        super(EditNodeMenu.class, node);
    }

    protected abstract ItemStack createTemplate();

    @Override
    public ItemStack checkedDraw(InventoryMenu menu) {
        if (template == null) {
            template = createTemplate();
        }
        return template.clone();
    }

    String getSkillDisplayName() {
        return getNode().getSkillName();
    }
}
