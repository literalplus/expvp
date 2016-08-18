/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.menu;

import com.google.common.base.Preconditions;
import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.TopRowMenu;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skill.tree.SimpleSkillTreeNode;
import me.minotopia.expvp.skill.tree.SkillTree;
import me.minotopia.expvp.skill.tree.ui.element.BackButton;
import me.minotopia.expvp.skill.tree.ui.element.SkillTreeIconElement;
import me.minotopia.expvp.skill.tree.ui.element.skill.EditableSkillElement;
import me.minotopia.expvp.skill.tree.ui.element.skill.NodeEditButton;
import me.minotopia.expvp.skill.tree.ui.element.skill.SubskillButton;
import me.minotopia.expvp.skill.tree.ui.renderer.NodeStructureRenderer;
import me.minotopia.expvp.skill.tree.ui.renderer.TreeStructureRenderer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
        this.node = Preconditions.checkNotNull(node, "node");
    }

    private EditNodeMenu(EPMenu parent, SimpleSkillTreeNode node) {
        super(parent.getPlugin(), node.getSkillName(), parent.getPlayer());
        this.parent = Preconditions.checkNotNull(parent, "parent");
        this.node = Preconditions.checkNotNull(node, "node");
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
        NodeEditButton nodeEditButton = new NodeEditButton(node);
        addToTopRow(0, backButton);
        addToTopRow(1, treeIcon);
        addToTopRow(2, nodeEditButton);
        addToTopRow(3, new SubskillButton(node, 0));
        addToTopRow(4, new SubskillButton(node, 1));
        addToTopRow(5, new SubskillButton(node, 2));
        addToTopRow(6, nodeEditButton);
        addToTopRow(7, treeIcon);
        addToTopRow(8, backButton);
    }

    @Override
    public void redraw() {
        attemptRender();
        super.redraw();
    }

    private void attemptRender() {
        try {
            clear();
            initTopRow();
            new NodeStructureRenderer(node, this, EditableSkillElement::new).render();
        } catch (Exception e) {
            e.printStackTrace();
            getPlayer().sendMessage(
                    "§e§lWarnung: §eDieser Skilltree konnte nicht vollständig gerendert werden. " +
                            "Spieler können diesen daher nicht sehen! Bitte entferne den " +
                            "äußersten Skill und versuche es nochmal. (Dies sollte nicht passieren)");
        }
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
        SimpleSkillTreeNode child = parent.createChild();
        if(isTreeNotRenderable(parent)) {
            rollbackChildAddBecauseNotRenderable(parent, child);
            return;  // ^ this is kinda brute-force, but checking before is too complicated
        }
        openSelectSkillMenu(skill -> {
            child.setValue(skill);
            getPlayer().sendMessage("§e§l➩ §aNeuer Subskill erstellt.");
            open(); //return to this menu in case they want to add more
        });
    }

    private boolean isTreeNotRenderable(SimpleSkillTreeNode parent) {
        return (parent.getTree().getHeight() > TreeStructureRenderer.MAX_HEIGHT) ||
                (parent.getTree().getWidth() > TreeStructureRenderer.MAX_WIDTH);
    }

    private void rollbackChildAddBecauseNotRenderable(SimpleSkillTreeNode parent, SimpleSkillTreeNode child) {
        parent.removeChild(child);
        getPlayer().sendMessage("§c§lFehler: §cDiese Aktion ist nicht durchführbar, weil der Baum sonst nicht mehr in ein Inventar passen würde!");
        getPlayer().closeInventory();
    }

    public void openSelectSkillMenu(Consumer<Skill> callback) {
        SelectSkillMenu.openNew(getPlugin(), getPlugin().getSkillManager(), getPlayer(), callback);
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
