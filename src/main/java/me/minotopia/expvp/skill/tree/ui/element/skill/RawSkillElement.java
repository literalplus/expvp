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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * Represents a skill in a menu, with just the skill name on the item and a custom click handler.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class RawSkillElement extends AbstractNodeElement<InventoryMenu> {
    private final Consumer<InventoryMenu> clickHandler;

    public RawSkillElement(SimpleSkillTreeNode node, Consumer<InventoryMenu> clickHandler) {
        super(InventoryMenu.class, node);
        this.clickHandler = clickHandler;
    }

    @Override
    public ItemStack checkedDraw(InventoryMenu menu) {
        return drawRaw("");
    }

    @Override
    public void checkedHandleMenuClick(InventoryClickEvent inventoryClickEvent, InventoryMenu inventoryMenu) {
        clickHandler.accept(inventoryMenu);
    }
}
