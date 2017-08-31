/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
}
