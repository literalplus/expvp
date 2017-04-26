/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
