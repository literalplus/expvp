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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

/**
 * Displays information about a tree element along with the viewing player's current amount of talent points.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-24
 */
public class TreeInfoElement implements MenuElement {
    private final SkillTree tree;
    private final Supplier<Integer> talentPointCountSupplier;

    public TreeInfoElement(SkillTree tree, Supplier<Integer> talentPointCountSupplier) {
        this.tree = tree;
        this.talentPointCountSupplier = talentPointCountSupplier;
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        return new ItemStackFactory(tree.getIconStack())
                .amount(talentPointCountSupplier.get())
                .produce();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        //no-op
    }
}
