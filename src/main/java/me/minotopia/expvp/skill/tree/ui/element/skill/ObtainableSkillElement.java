/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.element.skill;

import li.l1t.common.intake.exception.UserException;
import li.l1t.common.inventory.gui.InventoryMenu;
import me.minotopia.expvp.api.service.SkillObtainmentService;
import me.minotopia.expvp.skill.meta.SkillManager;
import me.minotopia.expvp.skill.tree.SimpleSkillTreeNode;
import me.minotopia.expvp.skill.tree.ui.menu.SkillTreeMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A simple menu element that represents a skill.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-22
 */
public class ObtainableSkillElement extends AbstractNodeElement<SkillTreeMenu> {

    public ObtainableSkillElement(SimpleSkillTreeNode node) {
        super(SkillTreeMenu.class, node);
    }

    @Override
    public ItemStack checkedDraw(InventoryMenu menu) {
        if (node.getValue() == null) {
            return new ItemStack(Material.BARRIER);
        }
        SkillManager manager = getSkillManagerFromValue();
        return manager.createSkillIconFor(node, menu.getPlayer());
    }

    @Override
    public void checkedHandleMenuClick(InventoryClickEvent inventoryClickEvent, SkillTreeMenu inventoryMenu) {
        if (node.getValue() == null) {
            throw new UserException("Da ist nichts!");
        }
        Player player = inventoryMenu.getPlayer();
        SkillObtainmentService obtainmentService = inventoryMenu.getPlugin().getSkillObtainmentService();
        obtainmentService.addObtainedSkill(player.getUniqueId(), node.getValue()); //handles cost etc
    }

}
