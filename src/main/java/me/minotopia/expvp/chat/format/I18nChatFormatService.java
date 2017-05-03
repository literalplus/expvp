/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.chat.format;

import com.google.common.collect.ObjectArrays;
import com.google.inject.Inject;
import li.l1t.common.chat.ComponentSender;
import li.l1t.common.chat.XyComponentBuilder;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.chat.ChatFormatService;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.score.league.Capability;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.score.league.LeagueService;
import me.minotopia.expvp.i18n.I18n;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.Locale;

/**
 * A chat format service that provides links to stats on names and international league names as prefixes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-03
 */
public class I18nChatFormatService implements ChatFormatService {
    private final Server server;
    private final DisplayNameService names;
    private final LeagueService leagues;

    @Inject
    public I18nChatFormatService(Server server, DisplayNameService names, LeagueService leagues) {
        this.server = server;
        this.names = names;
        this.leagues = leagues;
    }

    @Override
    public void sendToAll(Player player, String message) {
        League league = leagues.getCurrentLeague(player);
        BaseComponent[] germanMessage = createMessage(Locale.GERMAN, league, player, message);
        BaseComponent[] englishMessage = createMessage(Locale.ENGLISH, league, player, message);
        doSend(germanMessage, englishMessage);
    }

    private void doSend(BaseComponent[] germanMessage, BaseComponent[] englishMessage) {
        server.getOnlinePlayers().forEach(receiver -> {
            if (isGermanPlayer(receiver)) {
                ComponentSender.sendTo(germanMessage, receiver);
            } else {
                ComponentSender.sendTo(englishMessage, receiver);
            }
        });
        ComponentSender.sendTo(englishMessage, server.getConsoleSender());
    }

    private boolean isGermanPlayer(Player player) {
        return I18n.getLocaleFor(player.getUniqueId()).getISO3Language().equals(Locale.GERMAN.getISO3Language());
    }

    private BaseComponent[] createMessage(Locale locale, League league, Player player, String message) {
        BaseComponent[] leagueName = TextComponent.fromLegacyText(I18n.loc(locale, names.displayName(league)));
        BaseComponent[] playerName = new XyComponentBuilder(" ")
                .append(player.getName()).color(ChatColor.GRAY)
                .command("/stats " + player.getUniqueId())
                .tooltip(I18n.loc(locale, "core!stats-link", player.getName())).create();
        BaseComponent[] coreMessage = TextComponent.fromLegacyText(I18n.loc(
                locale, names.chatFormat(league, parseMessage(league, message))
        ));
        return ObjectArrays.concat(ObjectArrays.concat(leagueName, playerName, BaseComponent.class), coreMessage, BaseComponent.class);
    }

    private String parseMessage(League league, String message) {
        if (league.may(Capability.CHAT_COLOR)) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }

    @Override
    public void sendToAll(Player player, Message message) {
        League league = leagues.getCurrentLeague(player);
        BaseComponent[] germanMessage = createMessage(Locale.GERMAN, league, player, I18n.loc(Locale.GERMAN, message));
        BaseComponent[] englishMessage = createMessage(Locale.ENGLISH, league, player, I18n.loc(Locale.ENGLISH, message));
        doSend(germanMessage, englishMessage);
    }
}
