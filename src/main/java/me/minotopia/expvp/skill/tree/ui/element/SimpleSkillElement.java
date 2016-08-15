/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.element;

import com.google.common.base.Preconditions;
import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import me.minotopia.expvp.skill.meta.SkillManager;
import me.minotopia.expvp.skill.tree.SkillTreeNode;
import me.minotopia.expvp.skill.tree.ui.menu.SkillTreeInventoryMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A simple menu element that represents a skill.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-22
 */
public class SimpleSkillElement implements MenuElement<SkillTreeInventoryMenu> {
    private final SkillTreeNode<?> node;

    public SimpleSkillElement(SkillTreeNode<?> node) {
        this.node = node;
    }

    @Override
    public ItemStack draw(ElementHolder elementHolder) {
        //TODO: show completion status and availability
        if(node.getValue() == null) {
            return new ItemStack(Material.BARRIER);
        }
        SkillManager manager = node.getValue().getManager();
        Preconditions.checkNotNull(manager, "manager?!");
        Preconditions.checkArgument(elementHolder instanceof InventoryMenu, "can only render in InventoryMenu, is: %s", elementHolder.getClass());
        return manager.createSkillIconFor(node, ((InventoryMenu) elementHolder).getPlayer());
    }

    @Override
    public void handleMenuClick(InventoryClickEvent inventoryClickEvent, SkillTreeInventoryMenu inventoryMenu) {
        //TODO: actual handling
    }

    public SkillTreeNode<?> getNode() {
        return node;
    }
}
