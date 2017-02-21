/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.menu;

import li.l1t.common.inventory.gui.SimpleInventoryMenu;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.skilltree.SkillTree;
import me.minotopia.expvp.ui.element.SkillTreeElement;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * A menu for selection of trees.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-20
 */
public class SelectTreeMenu extends SimpleInventoryMenu implements EPMenu {
    private final Consumer<SkillTree> clickHandler;

    private SelectTreeMenu(EPPlugin plugin, String inventoryTitle, Player player, Consumer<SkillTree> clickHandler) {
        super(plugin, inventoryTitle, player);
        this.clickHandler = clickHandler;
    }

    @Override
    public EPPlugin getPlugin() {
        return (EPPlugin) super.getPlugin();
    }

    private void populate(Collection<SkillTree> trees) {
        trees.forEach(tree -> addElement(tree.getSlotId(), new SkillTreeElement(clickHandler, tree)));
    }

    public static SelectTreeMenu openNew(EPPlugin plugin, Player player, Consumer<SkillTree> clickHandler) {
        SelectTreeMenu menu = new SelectTreeMenu(plugin, "§6§lSkilltrees", player, clickHandler);
        menu.populate(plugin.getSkillTreeManager().getAll());
        menu.open();
        return menu;
    }
}
