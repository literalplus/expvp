/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.element;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import me.minotopia.expvp.skilltree.SkillTree;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * An element that displays the icon of a skill tree and forwards click events to a consumer.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-18
 */
public class SkillTreeElement implements MenuElement {
    private final Consumer<SkillTree> consumer;
    private final SkillTree tree;
    private final ItemStack icon;

    public SkillTreeElement(Consumer<SkillTree> consumer, SkillTree tree, ItemStack icon) {
        this.consumer = consumer;
        this.tree = tree;
        this.icon = icon;
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        return icon.clone();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent inventoryClickEvent, InventoryMenu inventoryMenu) {
        consumer.accept(tree);
    }
}
