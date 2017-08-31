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

import li.l1t.common.chat.CapsFilterService;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.chat.ChatMessageEvent;
import me.minotopia.expvp.api.chat.ChatPhase;

import java.util.Locale;

/**
 * Handles lowercasing of chat messages that contain too many uppercase characters.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-22
 */
public class CapsFilterHandler extends AbstractChatHandler {
    private final CapsFilterService service;

    public CapsFilterHandler(CapsFilterService service) {
        super(ChatPhase.CENSORING);
        this.service = service;
    }

    @Override
    public void handle(ChatMessageEvent evt) {
        if (service.check(evt.getMessage()) && !evt.mayBypassFilters()) {
            evt.setMessage(evt.getMessage().toLowerCase(Locale.GERMAN));
            evt.respond(Message.of("chat!caps.warn"));
        }
    }
}
