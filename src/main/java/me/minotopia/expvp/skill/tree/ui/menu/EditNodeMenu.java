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
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.element.Placeholder;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.skill.tree.SimpleSkillTreeNode;
import me.minotopia.expvp.skill.tree.ui.element.BackButton;
import me.minotopia.expvp.skill.tree.ui.element.SkillTreeIconElement;
import me.minotopia.expvp.skill.tree.ui.element.skill.RawSkillElement;
import me.minotopia.expvp.skill.tree.ui.element.skill.SubskillButton;
import me.minotopia.expvp.skill.tree.ui.renderer.NodeStructureRenderer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * An inventory menu that provides the frontend for editing a skill tree node.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-22
 */
public class EditNodeMenu extends TopRowMenu {
    private static final BiConsumer<InventoryClickEvent, InventoryMenu> NOOP_BICONSUMER = (thing1, thing2) -> {
    };
    private static final Consumer<InventoryMenu> NOOP_CONSUMER = (thing) -> {
    };
    private final SkillTreeMenu parent;
    private final SimpleSkillTreeNode node;

    private EditNodeMenu(SkillTreeMenu parent, SimpleSkillTreeNode node) {
        super(parent.getPlugin(), node.getDisplayName(), parent.getPlayer());
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
        addToTopRow(0, new BackButton(parent));
        addToTopRow(1, new SkillTreeIconElement(NOOP_BICONSUMER, node.getTree()));
        addToTopRow(2, new Placeholder(
                new ItemStackFactory(Material.BOOK_AND_QUILL)
                        .displayName("§6§lBearbeiten: §a" + node.getDisplayName())
                        .produce()
        ));
        addToTopRow(3, new SubskillButton(node, 0));
        addToTopRow(4, new SubskillButton(node, 1));
        addToTopRow(5, new SubskillButton(node, 2));
    }

    @Override
    public void redraw() {
        new NodeStructureRenderer(node, this, node -> new RawSkillElement(node, NOOP_CONSUMER));
        super.redraw();
    }

    private MenuElement createTreeElement(SimpleSkillTreeNode node) {
        return new RawSkillElement(node, inventoryMenu -> {
            //no-op
        });
    }

    public static EditNodeMenu openNew(SkillTreeMenu parent, SimpleSkillTreeNode node) {
        EditNodeMenu menu = new EditNodeMenu(parent, node);
        menu.open();
        return menu;
    }

    public SkillTreeMenu getParent() {
        return parent;
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
}
