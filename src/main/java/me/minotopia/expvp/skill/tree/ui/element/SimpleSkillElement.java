/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.element;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import me.minotopia.expvp.skill.tree.SkillTreeNode;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A simple menu element that represents a skill.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-22
 */
public class SimpleSkillElement implements MenuElement {
    private final SkillTreeNode<?> node;

    public SimpleSkillElement(SkillTreeNode<?> node) {
        this.node = node;
    }

    @Override
    public ItemStack draw(ElementHolder elementHolder) {
        //TODO: show completion status and availability
        return node.getValue().getIconStack();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent inventoryClickEvent, InventoryMenu inventoryMenu) {
        //TODO: actual handling
    }
}
