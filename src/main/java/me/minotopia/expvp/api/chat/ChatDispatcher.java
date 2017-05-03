/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.chat;

import me.minotopia.expvp.chat.dispatch.SimpleChatDispatcher;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Dispatches chat events to registered handlers and constructs the final message.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-21
 */
public interface ChatDispatcher {
    SimpleChatDispatcher registerHandler(ChatHandler handler);

    /**
     * Unregisters and disables a handler.
     *
     * @param handler the handler
     */
    void unregisterHandler(ChatHandler handler);

    void dispatchEvent(AsyncPlayerChatEvent event);
}
