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
