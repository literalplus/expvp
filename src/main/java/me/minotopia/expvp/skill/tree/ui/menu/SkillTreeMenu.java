/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.menu;

import com.google.common.base.Preconditions;
import li.l1t.common.intake.exception.InternalException;
import li.l1t.common.inventory.gui.SimpleInventoryMenu;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.skill.tree.SkillTree;
import me.minotopia.expvp.skill.tree.ui.element.skill.EditableSkillElement;
import me.minotopia.expvp.skill.tree.ui.renderer.TreeStructureRenderer;
import me.minotopia.expvp.skill.tree.ui.renderer.exception.RenderingException;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * An inventory menu that provides the frontend for a skill tree.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-22
 */
public class SkillTreeMenu extends SimpleInventoryMenu implements EPMenu {
    private TreeStructureRenderer renderer;

    SkillTreeMenu(EPPlugin plugin, Player player, TreeStructureRenderer renderer) {
        super(plugin, renderer.getTree().getDisplayName(), player);
        Preconditions.checkNotNull(renderer, "renderer");
        this.renderer = renderer;
    }

    private void applyRenderer() {
        if(!renderer.isRendered()) {
            attemptRender(renderer);
        }
        renderer.applyStructureTo(this);
    }

    private void attemptRender(TreeStructureRenderer renderer) {
        try {
            renderer.render();
        } catch (RenderingException e) {
            throw new InternalException("Konnte Skilltree nicht rendern", e);
        }
    }

    public static SkillTreeMenu openForEditing(EPPlugin plugin, Player player, SkillTree tree) {
        TreeStructureRenderer renderer = new TreeStructureRenderer(tree);
        renderer.setElementSupplier(EditableSkillElement::new);
        SkillTreeMenu menu = new SkillTreeMenu(plugin, player, renderer);
        menu.applyRenderer();
        menu.open();
        return menu;
    }

    @Override
    protected ItemStackFactory getPlaceholderFactory() {
        return new ItemStackFactory(Material.IRON_FENCE)
                .displayName("§7§l*");
    }

    @Override
    public EPPlugin getPlugin() {
        return (EPPlugin) super.getPlugin();
    }
}
