/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.menu;

import com.google.common.base.Preconditions;
import li.l1t.common.exception.NonSensitiveException;
import li.l1t.common.inventory.gui.SimpleInventoryMenu;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nInternalException;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Abstract base class for Expvp menus.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-06
 */
class AbstractEPMenu extends SimpleInventoryMenu implements EPMenu {
    private final List<Consumer<EPMenu>> closeHandlers = new ArrayList<>();

    public AbstractEPMenu(Plugin plugin, String inventoryTitle, Player player) {
        super(plugin, inventoryTitle, player);
    }

    @Override
    public EPPlugin getPlugin() {
        return (EPPlugin) super.getPlugin();
    }

    @Override
    public void handleClose(InventoryCloseEvent evt) {
        super.handleClose(evt);
        closeHandlers.forEach(handler -> handler.accept(this));
    }

    public void addCloseHandler(Consumer<EPMenu> handler) {
        Preconditions.checkNotNull(handler, "handler");
        closeHandlers.add(handler);
    }

    @Override
    public boolean handleClick(InventoryClickEvent evt) {
        try {
            return handleClick(evt);
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
}
