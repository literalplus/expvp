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
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.chat.ChatMessageEvent;
import me.minotopia.expvp.api.chat.ChatPhase;
import me.minotopia.expvp.api.chat.GlobalMuteService;

/**
 * Handles blocking global messages while global mute is enabled.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-07
 */
public class GlobalMuteHandler extends AbstractChatHandler {
    private final GlobalMuteService service;

    @Inject
    protected GlobalMuteHandler(GlobalMuteService service) {
        super(ChatPhase.BLOCKING);
        this.service = service;
    }

    @Override
    public void handle(ChatMessageEvent evt) {
        if (service.isEnabled() && !Permission.CHAT_GLOBAL_MUTE.has(evt.getPlayer())) {
            Message reasonMessage = findReasonMessage();
            evt.tryDenyMessage(Message.of("chat!glomu.blocked", reasonMessage), this);
        }
    }

    public Message findReasonMessage() {
        return service.getReason()
                .map(reason -> Message.of("chat!glomu.reason", reason))
                .orElseGet(() -> Message.ofText(""));
    }
}
