/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.menu;

import li.l1t.common.inventory.gui.PaginationListMenu;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skill.meta.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

/**
 * An inventory menu that allows to select a skill from the list of all available skills and executes
 * a custom handler when a skill is selected.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-18
 */
public class SelectSkillMenu extends PaginationListMenu<Skill> {
    private final Consumer<Skill> callback;

    public SelectSkillMenu(Plugin plugin, Player player, Consumer<Skill> callback) {
        super(plugin, player);
        this.callback = callback;
    }

    @Override
    protected void handleValueClick(Skill skill, InventoryClickEvent inventoryClickEvent) {
        callback.accept(skill);
        getPlayer().closeInventory();
    }

    @Override
    protected ItemStack drawItem(Skill skill) {
        return skill.getDisplayStack();
    }

    @Override
    protected String formatTitle(int pageNum, int pageCount) {
        return String.format("§9§lSkill auswählen §9(%d/%d)", pageCount, pageCount);
    }

    @Override
    public void handleClose(InventoryCloseEvent evt) {
        getPlayer().sendMessage("§e§l➩ §eSkillauswahl abgebrochen.");
    }

    public static SelectSkillMenu openNew(EPPlugin plugin, SkillManager manager, Player player, Consumer<Skill> callback) {
        SelectSkillMenu menu = new SelectSkillMenu(plugin, player, callback);
        menu.addItems(manager.getAll());
        menu.open();
        return menu;
    }
}
