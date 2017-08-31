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

package me.minotopia.expvp.misc;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.misc.PlayerInitService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Calls and manages player (de-)initialisation tasks via the Bukkit EventHandler API.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-05
 */
@Singleton
public class EPPlayerInitService implements PlayerInitService, Listener {
    private final List<Consumer<Player>> initHandlers = new ArrayList<>();
    private final List<Consumer<Player>> deInitHandlers = new ArrayList<>();

    @Inject
    public EPPlayerInitService() {
    }

    @Override
    public void callInitHandlers(Player player) {
        initHandlers.forEach(handler -> handler.accept(player));
    }

    @Override
    public void registerInitHandler(Consumer<Player> handler) {
        Preconditions.checkNotNull(handler, "handler");
        initHandlers.add(handler);
    }

    @Override
    public void callDeInitHandlers(Player player) {
        deInitHandlers.forEach(handler -> handler.accept(player));
    }

    @Override
    public void registerDeInitHandler(Consumer<Player> handler) {
        Preconditions.checkNotNull(handler, "handler");
        deInitHandlers.add(handler);
    }

    public static class PlayerInitListener implements Listener {
        private final PlayerInitService service;

        @Inject
        public PlayerInitListener(PlayerInitService service) {
            this.service = service;
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onPlayerJoin(PlayerJoinEvent event) {
            service.callInitHandlers(event.getPlayer());
        }

        @EventHandler(ignoreCancelled = true)
        public void onPlayerQuit(PlayerQuitEvent event) {
            service.callDeInitHandlers(event.getPlayer());
        }
    }
}
