/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.menu;

import com.google.common.base.Preconditions;
import li.l1t.common.exception.InternalException;
import li.l1t.common.exception.NonSensitiveException;
import li.l1t.common.inventory.gui.SimpleInventoryMenu;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nInternalException;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import me.minotopia.expvp.skilltree.SkillTree;
import me.minotopia.expvp.ui.renderer.TreeStructureRenderer;
import me.minotopia.expvp.ui.renderer.exception.RenderingException;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * An inventory menu that provides the frontend for a skill tree.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
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
        if (!renderer.isRendered()) {
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

    public static SkillTreeMenu openForResearch(EPPlugin plugin, Player player, SkillTree tree) {
        TreeStructureRenderer renderer = new TreeStructureRenderer(tree);
        SkillTreeMenu menu = new SkillTreeMenu(plugin, player, renderer);
        menu.applyRenderer();
        openInTransaction(menu);
        return menu;
    }

    private static void openInTransaction(SkillTreeMenu menu) {
        SessionProvider sessionProvider = menu.getPlugin().getSessionProvider();
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            menu.open();
            scoped.commitIfLastAndChanged();
        } catch (Exception e) {
            throw sessionProvider.handleException(e);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected ItemStackFactory getPlaceholderFactory() {
        return new ItemStackFactory(Material.STAINED_GLASS_PANE)
                .legacyData((byte) 15)
                .displayName("§7§l*");
    }

    @Override
    public EPPlugin getPlugin() {
        return (EPPlugin) super.getPlugin();
    }

    @Override
    public boolean handleClick(InventoryClickEvent evt) {
        try {
            return super.handleClick(evt);
        } catch (I18nUserException | I18nInternalException e) {
            getPlayer().sendMessage(I18n.loc(getPlayer(), e.toMessage()));
            getPlayer().closeInventory();
            if (e.needsLogging()) {
                throw e;
            } else {
                return true;
            }
        } catch (NonSensitiveException e) {
            getPlayer().sendMessage(e.getColoredMessage());
            getPlayer().closeInventory();
            throw e;
        }
    }

    public SkillTree getTree() {
        return renderer.getTree();
    }
}
