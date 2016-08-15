/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.element;

import li.l1t.common.inventory.gui.holder.ElementHolder;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skill.tree.SkillTreeNode;
import me.minotopia.expvp.skill.tree.ui.menu.SkillTreeInventoryMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a skill in a menu. This kind of element can be clicked to edit it in an inventory
 * GUI.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class EditableSkillElement extends SimpleSkillElement {
    public EditableSkillElement(SkillTreeNode<?> node) {
        super(node);
    }

    @Override
    public ItemStack draw(ElementHolder elementHolder) {
        Skill skill = getNode().getValue();
        if(skill == null) {
            return new ItemStack(Material.BARRIER);
        }
        ItemStackFactory factory = skill.getManager().createRawSkillIconFor(skill, true);
        factory.lore("\n§aZum Bearbeiten klicken");
        return factory.produce();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent inventoryClickEvent, SkillTreeInventoryMenu inventoryMenu) {

    }
}
