/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.chat;

import com.google.common.collect.ObjectArrays;
import com.google.inject.Inject;
import li.l1t.common.chat.ComponentSender;
import li.l1t.common.chat.XyComponentBuilder;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.score.league.LeagueService;
import me.minotopia.expvp.i18n.I18n;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Locale;

/**
 * Listens for chat events and adds proper formatting to them.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-26
 */
public class ChatListener implements Listener {
    private final LeagueService leagues;
    private final DisplayNameService names;
    private final Server server;

    @Inject
    public ChatListener(LeagueService leagues, DisplayNameService names, Server server) {
        this.leagues = leagues;
        this.names = names;
        this.server = server;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        League league = leagues.getCurrentLeague(event.getPlayer());
        sendToAll(event, league);
    }

    private void sendToAll(AsyncPlayerChatEvent event, League league) {
        BaseComponent[] germanMessage = createMessage(event, Locale.GERMAN, league);
        BaseComponent[] englishMessage = createMessage(event, Locale.ENGLISH, league);
        server.getOnlinePlayers().forEach(player -> {
            if (isGermanPlayer(player)) {
                ComponentSender.sendTo(germanMessage, player);
            } else {
                ComponentSender.sendTo(englishMessage, player);
            }
        });
        ComponentSender.sendTo(englishMessage, server.getConsoleSender());
    }

    private boolean isGermanPlayer(Player player) {
        return I18n.getLocaleFor(player.getUniqueId()).getISO3Language().equals(Locale.GERMAN.getISO3Language());
    }

    private BaseComponent[] createMessage(AsyncPlayerChatEvent event, Locale locale, League league) {
        BaseComponent[] leagueName = TextComponent.fromLegacyText(I18n.loc(locale, names.displayName(league)));
        BaseComponent[] playerName = new XyComponentBuilder(" ")
                .append(event.getPlayer().getName()).color(ChatColor.GRAY)
                .command("/stats " + event.getPlayer().getUniqueId())
                .tooltip(I18n.loc(locale, "core!stats-link", event.getPlayer().getName())).create();
        BaseComponent[] message = TextComponent.fromLegacyText(I18n.loc(locale, names.chatFormat(league)));
        return ObjectArrays.concat(ObjectArrays.concat(leagueName, playerName, BaseComponent.class), message, BaseComponent.class);
    }
}
