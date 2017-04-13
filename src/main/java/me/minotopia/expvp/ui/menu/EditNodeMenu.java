/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.menu;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import li.l1t.common.inventory.gui.TopRowMenu;
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.element.Placeholder;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.skill.SkillService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import me.minotopia.expvp.skilltree.SkillTree;
import me.minotopia.expvp.skilltree.SkillTreeManager;
import me.minotopia.expvp.ui.element.BackButton;
import me.minotopia.expvp.ui.element.skill.EditableSkillElement;
import me.minotopia.expvp.ui.element.skill.NodeEditButton;
import me.minotopia.expvp.ui.element.skill.SubskillButton;
import me.minotopia.expvp.ui.renderer.NodeStructureRenderer;
import me.minotopia.expvp.ui.renderer.TreeStructureRenderer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An inventory menu that provides the frontend for editing a skill tree node.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-07-22
 */
public class EditNodeMenu extends TopRowMenu implements EPMenu {
    private final EPMenu parent;
    private final SimpleSkillTreeNode node;
    private final DisplayNameService names;
    private final SkillService skills;
    private final BiConsumer<EPMenu, SimpleSkillTreeNode> childFactory;
    private final SelectSkillMenu.Factory selectSkillFactory;
    private final SkillTreeManager treeManager;

    private EditNodeMenu(SimpleSkillTreeNode node, EPPlugin plugin, EPMenu parent, Player player,
                         DisplayNameService names, SkillService skills,
                         BiConsumer<EPMenu, SimpleSkillTreeNode> childFactory, SelectSkillMenu.Factory selectSkillFactory, SkillTreeManager treeManager) {
        super(plugin, I18n.loc(player, names.displayName(node.getValue())), player);
        this.parent = parent;
        this.node = Preconditions.checkNotNull(node, "node");
        this.names = names;
        this.skills = skills;
        this.childFactory = childFactory;
        this.selectSkillFactory = selectSkillFactory;
        this.treeManager = treeManager;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected ItemStackFactory getPlaceholderFactory() {
        return new ItemStackFactory(Material.STAINED_GLASS_PANE)
                .legacyData((byte) 15)
                .displayName("§7§l*");
    }

    @Override
    protected void initTopRow() {
        BackButton backButton = new BackButton(parent, getPlayer());
        Placeholder treeIcon = new Placeholder(treeManager.createIconFor(node.getTree(), getPlayer()));
        NodeEditButton nodeEditButton = new NodeEditButton(node, this, names);
        addToTopRow(0, backButton);
        addToTopRow(1, treeIcon);
        addToTopRow(2, nodeEditButton);
        addToTopRow(3, new SubskillButton(this, node, 0, names));
        addToTopRow(4, new SubskillButton(this, node, 1, names));
        addToTopRow(5, new SubskillButton(this, node, 2, names));
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
            getInventory().clear();
            initTopRow();
            new NodeStructureRenderer(node, this, editableElement()).render();
        } catch (Exception e) {
            e.printStackTrace();
            I18n.sendLoc(getPlayer(), Format.userError("admin!ui.skilledit.unrederable"));
        }
    }

    @NotNull
    private Function<SimpleSkillTreeNode, MenuElement> editableElement() {
        return node -> new EditableSkillElement(this, node, skills, childFactory);
    }

    @Override
    public EPPlugin getPlugin() {
        return (EPPlugin) super.getPlugin();
    }

    public void addSubskill(SimpleSkillTreeNode parent) {
        SimpleSkillTreeNode child = parent.createChild();
        if (isTreeNotRenderable(parent)) {
            rollbackChildAddBecauseNotRenderable(parent, child);
            return;  // ^ this is kinda brute-force, but checking before is too complicated
        }
        openSelectSkillMenu(skill -> {
            child.setValue(skill);
            I18n.sendLoc(getPlayer(), Format.success("admin!ui.skilledit.add-subskill-success"));
            saveTree();
            open(); //return to this menu in case they want to add more
        });
    }

    private boolean isTreeNotRenderable(SimpleSkillTreeNode parent) {
        return (parent.getTree().getHeight() > TreeStructureRenderer.MAX_HEIGHT) ||
                (parent.getTree().getWidth() > TreeStructureRenderer.MAX_WIDTH);
    }

    private void rollbackChildAddBecauseNotRenderable(SimpleSkillTreeNode parent, SimpleSkillTreeNode child) {
        parent.removeChild(child);
        I18n.sendLoc(getPlayer(), Format.userError("error!tree.prevented-render-fail"));
        getPlayer().closeInventory();
    }

    public void openSelectSkillMenu(Consumer<Skill> callback) {
        selectSkillFactory.openMenu(getPlayer(), callback);
    }

    public void saveTree() {
        try {
            getPlugin().getSkillTreeManager().save(node.getTree());
        } catch (IOException e) {
            e.printStackTrace(); //welp
            I18n.sendLoc(getPlayer(), Format.internalError("error!ui.skilledit.unable-to-save"));
        }
    }

    public static class Factory {
        private final EPPlugin plugin;
        private final SkillService skills;
        private final DisplayNameService names;
        private final SelectSkillMenu.Factory selectSkillFactory;
        private final SkillTreeManager treeManager;

        @Inject
        public Factory(EPPlugin plugin, SkillService skills, DisplayNameService names,
                       SelectSkillMenu.Factory selectSkillFactory, SkillTreeManager treeManager) {
            this.plugin = plugin;
            this.skills = skills;
            this.names = names;
            this.selectSkillFactory = selectSkillFactory;
            this.treeManager = treeManager;
        }

        public EditNodeMenu createRoot(Player player, SkillTree tree) {
            return new EditNodeMenu(
                    tree, plugin, null, player, names, skills, this::createWithParent, selectSkillFactory,
                    treeManager);
        }


        public EditNodeMenu createWithParent(EPMenu parent, SimpleSkillTreeNode node) {
            return new EditNodeMenu(
                    node, plugin, parent, parent.getPlayer(), names, skills, this::createWithParent, selectSkillFactory,
                    treeManager);
        }
    }
}
