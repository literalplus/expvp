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
import me.minotopia.expvp.skill.tree.SkillTree;
import me.minotopia.expvp.skill.tree.ui.element.EditableSkillElement;
import me.minotopia.expvp.skill.tree.ui.renderer.TreeStructureRenderer;
import me.minotopia.expvp.skill.tree.ui.renderer.exception.RenderingException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * An inventory menu that provides the frontend for a skill tree.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-22
 */
public class SkillTreeInventoryMenu extends SimpleInventoryMenu {
    private TreeStructureRenderer renderer;

    public SkillTreeInventoryMenu(Plugin plugin, Player player, SkillTree tree) {
        super(plugin, tree.getDisplayName(), player);
        this.renderer = new TreeStructureRenderer(tree);
    }

    public SkillTreeInventoryMenu(Plugin plugin, Player player, TreeStructureRenderer renderer) {
        super(plugin, renderer.getTree().getDisplayName(), player);
        setRenderer(renderer);
    }

    public void setRenderer(TreeStructureRenderer renderer) {
        Preconditions.checkNotNull(renderer, "renderer");
        this.renderer = renderer;
        if(renderer.isRendered()) {
            try {
                renderer.render();
            } catch (RenderingException e) {
                throw new InternalException("Konnte Skilltree nicht rendern", e);
            }
        }
        renderer.applyStructureTo(this);
    }

    @Override
    protected ItemStackFactory getPlaceholderFactory() {
        return new ItemStackFactory(Material.IRON_FENCE)
                .displayName("§7§l*");
    }

    public void enableEditing() {
        renderer.setElementSupplier(EditableSkillElement::new);
    }
}
