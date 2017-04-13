/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.element.skill;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
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
    private final DisplayNameService names;

    public SubskillButton(EditNodeMenu menu, SimpleSkillTreeNode parentNode, int childId, DisplayNameService names) {
        super(menu, parentNode);
        this.childId = childId;
        this.names = names;
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
        return localize("admin!ui.skilledit.subskill-title", childId);
    }

    private String formatLoreWithActionInfo() {
        StringBuilder lore = new StringBuilder("§a");
        if (subskillExists()) {
            lore.append(names.displayName(getSubskill().getValue())).append("\n\n");
            if (subskillHasChildren()) {
                lore.append(localize("admin!ui.skilledit.remove-children-first"));
            } else {
                lore.append(localize("admin!ui.skilledit.click-to-remove"));
            }
        } else {
            lore.append(localize("admin!ui.skilledit.click-to-add"));
        }
        return lore.toString();
    }

    private boolean subskillHasChildren() {
        return getSubskill().hasChildren();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        if (subskillExists()) {
            if (subskillHasChildren()) {
                I18n.sendLoc(menu.getPlayer(), Format.userError("admin!ui.skilledit.rm-children-first"));
            } else {
                I18n.sendLoc(menu.getPlayer(), Format.success("admin!ui.skilledit.rm-subskill-success"));
                removeThisChild();
            }
            menu.open();
        } else {
            getMenu().addSubskill(getParentNode());
        }
        getMenu().saveTree();
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
