/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
