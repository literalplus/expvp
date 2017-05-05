/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
