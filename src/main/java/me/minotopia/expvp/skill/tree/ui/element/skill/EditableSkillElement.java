/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.element.skill;

import li.l1t.common.inventory.gui.InventoryMenu;
import me.minotopia.expvp.skill.tree.SimpleSkillTreeNode;
import me.minotopia.expvp.skill.tree.ui.menu.EPMenu;
import me.minotopia.expvp.skill.tree.ui.menu.EditNodeMenu;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a skill in a menu. This kind of element can be clicked to edit it in an inventory
 * GUI.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class EditableSkillElement extends AbstractNodeElement<EPMenu> {
    public EditableSkillElement(SimpleSkillTreeNode node) {
        super(EPMenu.class, node);
    }

    @Override
    public ItemStack checkedDraw(InventoryMenu elementHolder) {
        return drawRaw("\n§aZum Bearbeiten klicken");
    }

    @Override
    public void checkedHandleMenuClick(InventoryClickEvent evt, EPMenu menu) {
        EditNodeMenu.openNew(menu, getNode());
    }
}
