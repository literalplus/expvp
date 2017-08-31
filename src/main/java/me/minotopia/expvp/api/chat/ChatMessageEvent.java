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

package me.minotopia.expvp.api.chat;

import li.l1t.common.i18n.Message;
import org.bukkit.entity.Player;

/**
 * Handles getting information about a chat message event and writing changes back to it.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-21
 */
public interface ChatMessageEvent {
    boolean isCancelled();

    void setCancelled(boolean cancelled);

    /**
     * Attempts to deny this chat message with an error message. This method fails if the player who
     * sent the message has {@link me.minotopia.expvp.Permission#ADMIN_CHAT_IGNORE}
     *
     * @param errorMessage the message to send, with {@link me.minotopia.expvp.i18n.Format#userError(Message)} applied
     * @param handler      the handler that caused this denial
     * @return whether the denial was successful
     */
    boolean tryDenyMessage(Message errorMessage, ChatHandler handler);

    /**
     * @return whether the player may bypass filters
     */
    boolean mayBypassFilters();

    /**
     * Sends a message to this event's player, without any format applied.
     *
     * @param message the message to send
     */
    void respond(Message message);

    /**
     * Marks this message for dropping, that is, no further processing will be applied to it.
     */
    void dropMessage();

    Player getPlayer();

    String getMessage();

    String getInitialMessage();

    void setMessage(String newMessage);

    /**
     * @return whether this event should be forwarded to any further handlers
     */
    boolean shouldContinueHandling();
}
