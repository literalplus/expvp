/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.misc;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.misc.PlayerInitService;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

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
    public EPPlayerInitService(Server server, Plugin plugin) {
        server.getPluginManager().registerEvents(this, plugin);
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

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        callInitHandlers(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        callDeInitHandlers(event.getPlayer());
    }
}
