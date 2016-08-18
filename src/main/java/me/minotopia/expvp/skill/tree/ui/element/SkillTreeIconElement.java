/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.element;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.element.LambdaMenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.skill.tree.SkillTree;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/**
 * An element that displays the icon of a skill tree and forwards click events to a consumer.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-18
 */
public class SkillTreeIconElement extends LambdaMenuElement<InventoryMenu> {
    private final ItemStack template;

    public SkillTreeIconElement(BiConsumer<InventoryClickEvent, InventoryMenu> consumer, SkillTree tree) {
        super(InventoryMenu.class, consumer, null);
        template = tree.getIconStack();
    }

    @Override
    public ItemStack checkedDraw(ElementHolder menu) {
        if(template == null) {
            return new ItemStackFactory(Material.BARRIER)
                    .displayName("§7<Kein Treeicon>")
                    .produce();
        }
        return template;
    }
}
