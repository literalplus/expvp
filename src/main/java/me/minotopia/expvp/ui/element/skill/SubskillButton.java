/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.element.skill;

import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import me.minotopia.expvp.ui.menu.EditNodeMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A button that allows to add or remove subskills of a skill.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-18
 */
public class SubskillButton extends EditButton {
    private final int childId;

    public SubskillButton(SimpleSkillTreeNode parentNode, int childId) {
        super(parentNode);
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
        return getParentNode().hasChild(childId);
    }

    private String formatDisplayName() {
        return "§6§lSubskill " + childId + ":";
    }

    private String formatLoreWithActionInfo() {
        StringBuilder lore = new StringBuilder("§a");
        if (subskillExists()) {
            lore.append(getSubskill().getSkillName()).append("\n\n");
            if (subskillHasChildren()) {
                lore.append("§cZuerst Subskills\n§centfernen, bevor dieser\n§centfernt werden kann.");
            } else {
                lore.append("§aKlicken zum Entfernen");
            }
        } else {
            lore.append("Klicken zum Hinzufügen");
        }
        return lore.toString();
    }

    private boolean subskillHasChildren() {
        return getSubskill().hasChildren();
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
            menu.open();
        } else {
            menu.addSubskill(getParentNode());
        }
        menu.saveTree();
    }

    private void removeThisChild() {
        getParentNode().removeChild(getParentNode().getChild(childId));
    }

    private SimpleSkillTreeNode getSubskill() {
        return getParentNode().getChild(childId);
    }

    private SimpleSkillTreeNode getParentNode() {
        return super.getNode(); //just an alias for readability
    }
}
