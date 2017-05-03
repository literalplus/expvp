/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.chat.handler;

import com.google.inject.Inject;
import me.minotopia.expvp.api.chat.ChatMessageEvent;
import me.minotopia.expvp.api.chat.ChatPhase;
import me.minotopia.expvp.api.score.league.Capability;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.score.league.LeagueService;
import net.md_5.bungee.api.ChatColor;

/**
 * Converts alternate color codes for permitted players.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-22
 */
public class ChatColorHandler extends AbstractChatHandler {
    private final LeagueService leagues;

    @Inject
    protected ChatColorHandler(LeagueService leagues) {
        super(ChatPhase.DECORATING);
        this.leagues = leagues;
    }

    @Override
    public void handle(ChatMessageEvent evt) {
        League league = leagues.getCurrentLeague(evt.getPlayer());
        if (league.may(Capability.CHAT_COLOR)) {
            evt.setMessage(ChatColor.translateAlternateColorCodes('&', evt.getMessage()));
        }
    }
}
