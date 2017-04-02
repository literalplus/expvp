/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.menu;

import com.google.inject.Inject;
import li.l1t.common.exception.InternalException;
import li.l1t.common.exception.NonSensitiveException;
import li.l1t.common.inventory.gui.SimpleInventoryMenu;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.score.TalentPointService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nInternalException;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import me.minotopia.expvp.skilltree.SkillTree;
import me.minotopia.expvp.ui.element.BackButton;
import me.minotopia.expvp.ui.element.TreeInfoElement;
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

    private SkillTreeMenu(EPPlugin plugin, Player player, SkillTree tree, Runnable backButtonHandler,
                          TalentPointService talentPoints) {
        super(plugin, tree.getDisplayName(), player);
        this.renderer = new TreeStructureRenderer(tree);
        if (backButtonHandler != null) {
            addElement(0, new BackButton(inventoryMenu -> backButtonHandler.run()));
        }
        addElement(1, new TreeInfoElement(tree, () -> talentPoints.getCurrentTalentPointCount(getPlayer())));
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
            if (e.needsLogging()) {
                throw e;
            } else {
                return true;
            }
        }
    }

    public SkillTree getTree() {
        return renderer.getTree();
    }

    public static class Factory {
        private final EPPlugin plugin;
        private final TalentPointService talentPointService;

        @Inject
        public Factory(EPPlugin plugin, TalentPointService talentPointService) {
            this.plugin = plugin;
            this.talentPointService = talentPointService;
        }

        public SkillTreeMenu createMenuWithBackButton(Player player, SkillTree tree, Runnable backButtonHandler) {
            SkillTreeMenu menu = new SkillTreeMenu(
                    plugin, player, tree, backButtonHandler, talentPointService
            );
            menu.applyRenderer();
            return menu;
        }

        public SkillTreeMenu createMenuWithoutBackButton(Player player, SkillTree tree) {
            return createMenuWithBackButton(player, tree, null);
        }

        public SkillTreeMenu openMenu(Player player, SkillTree tree, Runnable backButtonHandler) {
            SkillTreeMenu menu;
            if (backButtonHandler != null) {
                menu = createMenuWithBackButton(player, tree, backButtonHandler);
            } else {
                menu = createMenuWithoutBackButton(player, tree);
            }
            openInTransaction(menu);
            return menu;
        }
    }
}
