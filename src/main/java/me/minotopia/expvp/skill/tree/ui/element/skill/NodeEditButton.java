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
 * A button for editing the skill of a node itself.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-19
 */
public class NodeEditButton extends EditButton {
    public NodeEditButton(SimpleSkillTreeNode node) {
        super(node);
    }

    @Override
    protected ItemStack createTemplate() {
        return new ItemStackFactory(Material.BOOK_AND_QUILL)
                .displayName(formatDisplayName())
                .lore(formatLore())
                .produce();
    }

    private String formatLore() {
        return "§a" + getSkillDisplayName() +"\n\n§aHier klicken zum Ändern";
    }

    private String formatDisplayName() {
        return "§6§lBearbeiten:";
    }

    @Override
    protected void checkedHandleMenuClick(InventoryClickEvent evt, EditNodeMenu menu) {
        menu.openSelectSkillMenu(skill -> {
            getNode().setValue(skill);
            menu.getPlayer().sendMessage("§e§l➩ §aSkillzuweisung geändert.");
            menu.saveTree();
            menu.open();
        });
    }
}
