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

package me.minotopia.expvp.ui.element.main;

import li.l1t.common.i18n.Message;
import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.score.league.StaticLeague;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * A menu item that shows the next league for a player and the amount of Exp they still need until then.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-20
 */
public class NextLeagueElement implements MenuElement {
    private final ItemStack stack;

    public NextLeagueElement(Player player, League currentLeague, PlayerData data, DisplayNameService names) {
        Optional<League> next = currentLeague.next();
        ItemStackFactory factory = new ItemStackFactory(currentLeague.getDisplayMaterial());
        if (next.isPresent()) {
            League nextLeague = next.get();
            Message displayName = names.displayName(nextLeague);
            factory.displayName(I18n.loc(player, "core!inv.main.next-league", displayName));
            if (nextLeague == StaticLeague.BEDROCK) {
                factory.lore(I18n.loc(player, "core!inv.main.next-bedrock", displayName));
            } else {
                int neededExp = nextLeague.getMinExp() - data.getExp();
                factory.lore(I18n.loc(player, "core!inv.main.next-in", neededExp));
            }
        } else {
            factory.displayName(" ").lore(I18n.loc(player, "core!inv.main.next-none"));
        }
        stack = factory.produce();
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        return stack.clone();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {

    }
}
