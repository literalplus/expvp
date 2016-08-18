/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.menu;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.TopRowMenu;
import li.l1t.common.inventory.gui.element.Placeholder;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.skill.tree.SimpleSkillTreeNode;
import me.minotopia.expvp.skill.tree.SkillTree;
import me.minotopia.expvp.skill.tree.ui.element.BackButton;
import me.minotopia.expvp.skill.tree.ui.element.SkillTreeIconElement;
import me.minotopia.expvp.skill.tree.ui.element.skill.EditableSkillElement;
import me.minotopia.expvp.skill.tree.ui.element.skill.SubskillButton;
import me.minotopia.expvp.skill.tree.ui.renderer.NodeStructureRenderer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * An inventory menu that provides the frontend for editing a skill tree node.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-22
 */
public class EditNodeMenu extends TopRowMenu implements EPMenu {
    private static final BiConsumer<InventoryClickEvent, InventoryMenu> NOOP_BICONSUMER = (thing1, thing2) -> {
    };
    private final EPMenu parent;
    private final SimpleSkillTreeNode node;

    private EditNodeMenu(SimpleSkillTreeNode node, EPPlugin plugin, Player player) {
        super(plugin, node.getSkillName(), player);
        this.parent = null;
        this.node = node;
    }

    private EditNodeMenu(EPMenu parent, SimpleSkillTreeNode node) {
        super(parent.getPlugin(), node.getSkillName(), parent.getPlayer());
        this.parent = parent;
        this.node = node;
    }

    @Override
    protected ItemStackFactory getPlaceholderFactory() {
        return new ItemStackFactory(Material.IRON_FENCE)
                .displayName("§7§l*");
    }

    @Override
    protected void initTopRow() {
        BackButton backButton = new BackButton(parent);
        SkillTreeIconElement treeIcon = new SkillTreeIconElement(NOOP_BICONSUMER, node.getTree());
        Placeholder editDescription = new Placeholder(
                new ItemStackFactory(Material.BOOK_AND_QUILL)
                        .displayName("§6§lBearbeiten: §a" + node.getSkillName())
                        .produce()
        );
        addToTopRow(0, backButton);
        addToTopRow(1, treeIcon);
        addToTopRow(2, editDescription);
        addToTopRow(3, new SubskillButton(node, 0));
        addToTopRow(4, new SubskillButton(node, 1));
        addToTopRow(5, new SubskillButton(node, 2));
        addToTopRow(6, editDescription);
        addToTopRow(7, treeIcon);
        addToTopRow(8, backButton);
    }

    @Override
    public void redraw() {
        clear();
        initTopRow();
        new NodeStructureRenderer(node, this, EditableSkillElement::new).render();
        super.redraw();
    }

    public static EditNodeMenu openNew(EPMenu parent, SimpleSkillTreeNode node) {
        EditNodeMenu menu = new EditNodeMenu(parent, node);
        menu.open();
        return menu;
    }

    public static EditNodeMenu openNew(EPPlugin plugin, Player player, SkillTree tree) {
        EditNodeMenu menu = new EditNodeMenu(tree, plugin, player);
        menu.open();
        return menu;
    }

    @Override
    public EPPlugin getPlugin() {
        return (EPPlugin) super.getPlugin();
    }

    public void addSubskill(SimpleSkillTreeNode parent) {
        if(parent.getChildren().size() >= 3) {
            getPlayer().sendMessage("§c§lFehler: §cDieser Skill hat bereits drei Subskills!");
            getPlayer().closeInventory();
            return;
        }
        SimpleSkillTreeNode child = parent.createChild();
        SelectSkillMenu.openNew(getPlugin(), getPlugin().getSkillManager(), getPlayer(), skill -> {
            child.setValue(skill);
            getPlayer().sendMessage("§e§l➩ §aNeuer Subskill erstellt.");
            open(); //return to this menu in case they want to add more
        });
    }

    public void saveTree() {
        try {
            getPlugin().getSkillTreeManager().save(node.getTree());
        } catch (IOException e) {
            e.printStackTrace(); //welp
            getPlayer().sendMessage("§4§lInterner Fehler: §cKonnte Baum nicht speichern");
        }
    }
}
