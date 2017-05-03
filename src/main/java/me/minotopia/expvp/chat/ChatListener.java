/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.chat;

import com.google.inject.Inject;
import me.minotopia.expvp.api.chat.ChatDispatcher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Listens for chat events and adds proper formatting to them.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-26
 */
public class ChatListener implements Listener {
    private final ChatDispatcher dispatcher;

    @Inject
    public ChatListener(ChatDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        dispatcher.dispatchEvent(event);
    }
}
