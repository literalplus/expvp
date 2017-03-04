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
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.skilltree.SkillTree;
import org.bukkit.Material;
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
    private final ItemStack template;
    private final SkillTree tree;

    public SkillTreeElement(Consumer<SkillTree> consumer, SkillTree tree) {
        this.consumer = consumer;
        this.template = tree.getIconStack();
        this.tree = tree;
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        if (template == null) {
            return new ItemStackFactory(Material.BARRIER)
                    .displayName("§7<Kein Treeicon>")
                    .produce();
        }
        return template;
    }

    @Override
    public void handleMenuClick(InventoryClickEvent inventoryClickEvent, InventoryMenu inventoryMenu) {
        consumer.accept(tree);
    }
}
