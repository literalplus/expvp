/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.menu;

import com.google.inject.Inject;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.skilltree.SkillTree;
import me.minotopia.expvp.skilltree.SkillTreeManager;
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
public class SelectTreeMenu extends AbstractEPMenu {
    private final Consumer<SkillTree> clickHandler;
    private final SkillTreeManager manager;

    private SelectTreeMenu(EPPlugin plugin, String inventoryTitle, Player player, Consumer<SkillTree> clickHandler,
                           SkillTreeManager manager) {
        super(plugin, inventoryTitle, player);
        this.clickHandler = clickHandler;
        this.manager = manager;
    }

    private void populate(Collection<SkillTree> trees) {
        trees.forEach(tree -> addElement(
                tree.getSlotId(),
                new SkillTreeElement(clickHandler, tree, manager.createIconFor(tree, getPlayer()))
        ));
    }

    public static class Factory {
        private final EPPlugin plugin;
        private final SkillTreeManager treeManager;
        private final SkillTreeMenu.Factory treeMenuFactory;

        @Inject
        public Factory(EPPlugin plugin, SkillTreeManager treeManager, SkillTreeMenu.Factory treeMenuFactory) {
            this.plugin = plugin;
            this.treeManager = treeManager;
            this.treeMenuFactory = treeMenuFactory;
        }

        public SelectTreeMenu createMenu(Player player, Consumer<SkillTree> clickHandler) {
            String title = I18n.loc(player, "core!tree-select.title");
            SelectTreeMenu menu = new SelectTreeMenu(plugin, title, player, clickHandler, treeManager);
            menu.populate(treeManager.getAll());
            return menu;
        }

        public SelectTreeMenu openMenu(Player player, Consumer<SkillTree> clickHandler) {
            SelectTreeMenu menu = createMenu(player, clickHandler);
            menu.open();
            return menu;
        }

        public SelectTreeMenu openForResearch(Player player) {
            return openMenu(player, tree -> treeMenuFactory.openMenu(player, tree, () -> openForResearch(player)));
        }
    }
}
