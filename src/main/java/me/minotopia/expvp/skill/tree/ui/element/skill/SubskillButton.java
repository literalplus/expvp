/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.element.skill;

import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.skill.tree.SimpleSkillTreeNode;
import me.minotopia.expvp.skill.tree.ui.menu.EditNodeMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A button that allows to add or remove subskills of a skill.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-18
 */
public class SubskillButton extends EditButton {
    private final SimpleSkillTreeNode parentNode;
    private final int childId;

    public SubskillButton(SimpleSkillTreeNode parentNode, int childId) {
        super(parentNode);
        this.parentNode = parentNode;
        this.childId = childId;
    }

    @Override
    protected ItemStack createTemplate() {
        return new ItemStackFactory(getMaterialBasedOnWhetherTheSubskillExists())
                .displayName(formatDisplayName())
                .lore(formatLoreWithActionInfo())
                .produce();
    }

    private Material getMaterialBasedOnWhetherTheSubskillExists() {
        return subskillExists() ? Material.REDSTONE_TORCH_ON : Material.TORCH;
    }

    private boolean subskillExists() {
        return parentNode.hasChild(childId);
    }

    private String formatDisplayName() {
        return "§6§lSubskill " + childId + ":";
    }

    private String formatLoreWithActionInfo() {
        StringBuilder lore = new StringBuilder("§a");
        if (subskillExists()) {
            lore.append(getSkillDisplayName()).append("\n\n");
            if (subskillHasChildren()) {
                lore.append("§eZuerst Subskills\n§eentfernen, bevor dieser\n§eentfernt werden kann.");
            } else {
                lore.append("§cKlicken zum Entfernen");
            }
        } else {
            lore.append("Klicken zum Hinzufügen");
        }
        return lore.toString();
    }

    private boolean subskillHasChildren() {
        return getNode().hasChildren();
    }

    @Override
    public void checkedHandleMenuClick(InventoryClickEvent evt, EditNodeMenu menu) {
        if (subskillExists()) {
            if (subskillHasChildren()) {
                menu.getPlayer().sendMessage("§c§lFehler: §cDu musst zuerst die Subskills dieses Subskills entfernen, bevor du ihn selbst entfernen kannst!");
            } else {
                menu.getPlayer().sendMessage("§e§l➩ §aSubskill aus dem Baum entfernt: " + getSkillDisplayName());
                removeThisChild();
            }
        } else {
            menu.addSubskill(parentNode);
        }
        menu.redraw();
        menu.saveTree();
    }

    private void removeThisChild() {
        parentNode.removeChild(parentNode.getChild(childId));
    }

    @Override
    public SimpleSkillTreeNode getNode() {
        return parentNode.getChild(childId);
    }
}
