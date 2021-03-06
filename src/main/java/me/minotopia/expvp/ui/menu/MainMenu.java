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

import com.google.inject.Inject;
import li.l1t.common.inventory.SlotPosition;
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.element.Placeholder;
import li.l1t.common.shared.uuid.UUIDRepository;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.friend.FriendService;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompiler;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.score.league.LeagueService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.ui.element.main.*;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.stream.IntStream;

/**
 * The main inventory menu for navigation in Expvp.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-20
 */
public class MainMenu extends AbstractEPMenu {
    private final LeagueService leagues;
    private final DisplayNameService names;
    private final KitCompiler kitCompiler;
    private final UUIDRepository uuidRepository;
    private final SelectTreeMenu.Factory selectTreeMenuFactory;

    public MainMenu(Plugin plugin, Player player, LeagueService leagues, DisplayNameService names,
                    KitCompiler kitCompiler, UUIDRepository uuidRepository, SelectTreeMenu.Factory selectTreeMenuFactory) {
        super(plugin, I18n.loc(player, "core!inv.main.title"), player);
        this.leagues = leagues;
        this.names = names;
        this.kitCompiler = kitCompiler;
        this.uuidRepository = uuidRepository;
        this.selectTreeMenuFactory = selectTreeMenuFactory;
    }

    public void populate(PlayerData ownData, PlayerData friendData) {
        addArmorElements(kitCompiler.compile(getPlayer(), ownData));
        makePlaceholderColumn(0, 1, 3);
        addHumanElements(ownData, 0);
        addHumanElements(friendData, 1);
        makePlaceholderColumn(0, 1, 7);
        League ownLeague = leagues.getPlayerLeague(ownData);
        addElementXY(8, 0, new NextLeagueElement(getPlayer(), ownLeague, ownData, names));
        addElementXY(8, 1, new DeathExpElement(getPlayer(), ownLeague));
        makePlaceholderRow(2);
        addElementXY(2, 4, new StatsTopLinkButton(getPlayer()));
        addElementXY(6, 4, new TreeSelectLinkButton(getPlayer(), ownData, selectTreeMenuFactory));
    }

    private void makePlaceholderColumn(int fromY, int toY, int x) {
        IntStream.range(fromY, toY + 1).forEach(y -> addPlaceholder(SlotPosition.ofXY(x, y)));
    }

    private void makePlaceholderRow(int y) {
        IntStream.range(0, SlotPosition.SLOTS_PER_ROW).forEach(x -> addPlaceholder(SlotPosition.ofXY(x, y)));
    }

    private void addArmorElements(KitCompilation kit) {
        addElementXY(0, 0, kitElementDisplay(kit, 39)); /* helmet */
        addElementXY(1, 0, kitElementDisplay(kit, 38));
        addElementXY(0, 1, kitElementDisplay(kit, 37));
        addElementXY(1, 1, kitElementDisplay(kit, 36));
        addElementXY(2, 0, new ArmorColorLinkButton(getPlayer()));
        addElementXY(2, 1, new ArmorResetButton(getPlayer()));
    }

    private void addHumanElements(PlayerData subject, int row) {
        if (subject != null) {
            addElementXY(4, row, new PlayerHeadElement(getPlayer(), uuidRepository.getName(subject.getUniqueId())));
            addElementXY(5, row, new LeagueBlockElement(getPlayer(), leagues.getPlayerLeague(subject), names));
            addElementXY(6, row, new ExpDisplayElement(getPlayer(), subject));
        } else {
            addElementXY(4, row, new PlayerHeadElement(getPlayer(), null));
            addElementXY(5, row, noFriendPlaceholder());
            addElementXY(6, row, noFriendPlaceholder());
        }
    }

    private Placeholder noFriendPlaceholder() {
        return new Placeholder(new ItemStackFactory(Material.IRON_FENCE)
                .displayName("§c???").lore(I18n.loc(getPlayer(), "core!inv.main.no-friend"))
                .produce());
    }

    private void addElementXY(int x, int y, MenuElement element) {
        addElement(SlotPosition.ofXY(x, y), element);
    }

    private Placeholder kitElementDisplay(KitCompilation kit, int slotId) {
        return new Placeholder(kit.snapshotOf(slotId).toItemStack());
    }

    @Override
    protected ItemStackFactory getPlaceholderFactory() {
        return new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15))
                .displayName("§7§l*");
    }

    public static class Factory {
        private final SessionProvider sessionProvider;
        private final Plugin plugin;
        private final LeagueService leagues;
        private final DisplayNameService names;
        private final KitCompiler kitCompiler;
        private final UUIDRepository uuidRepository;
        private final SelectTreeMenu.Factory selectTreeMenuFactory;
        private final FriendService friendService;
        private final PlayerDataService players;

        @Inject
        public Factory(SessionProvider sessionProvider, Plugin plugin, LeagueService leagues, DisplayNameService names,
                       KitCompiler kitCompiler, UUIDRepository uuidRepository, SelectTreeMenu.Factory selectTreeMenuFactory,
                       FriendService friendService, PlayerDataService players) {
            this.sessionProvider = sessionProvider;
            this.plugin = plugin;
            this.leagues = leagues;
            this.names = names;
            this.kitCompiler = kitCompiler;
            this.uuidRepository = uuidRepository;
            this.selectTreeMenuFactory = selectTreeMenuFactory;
            this.friendService = friendService;
            this.players = players;
        }

        public MainMenu createMenuFor(Player player) {
            return sessionProvider.inSessionAnd(ignored -> {
                MainMenu menu = new MainMenu(
                        plugin, player, leagues, names, kitCompiler, uuidRepository, selectTreeMenuFactory
                );
                PlayerData ownData = players.findOrCreateData(player.getUniqueId());
                PlayerData friendData = friendService.findFriend(player).orElse(null);
                menu.populate(ownData, friendData);
                return menu;
            });
        }

        public MainMenu openMenuFor(Player player) {
            MainMenu menu = createMenuFor(player);
            menu.open();
            return menu;
        }
    }
}
