/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

import com.google.inject.Inject;
import me.minotopia.expvp.EPPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listens for players joining and leaving to keep the Internationalisation caches up to date.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
class LocaleJoinLeaveListener implements Listener {
    private final EPPlugin plugin;
    private final LocaleService localeService;

    @Inject
    LocaleJoinLeaveListener(EPPlugin plugin, LocaleService localeService) {
        this.plugin = plugin;
        this.localeService = localeService;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.async(() -> localeService.recomputeClientLocale(player));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        I18n.clearLocaleOf(event.getPlayer().getUniqueId());
    }
}
