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

package me.minotopia.expvp.chat.dispatch;

import com.google.common.base.Preconditions;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.chat.*;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.util.SessionProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

/**
 * A simple implementation of a chat dispatcher.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-21
 */
@Singleton
public class SimpleChatDispatcher implements ChatDispatcher {
    private static final Logger LOGGER = LogManager.getLogger(SimpleChatDispatcher.class);
    private ListMultimap<ChatPhase, ChatHandler> handlerMap = MultimapBuilder
            .enumKeys(ChatPhase.class)
            .arrayListValues()
            .build();
    private final ChatFormatService formatService;
    private final SessionProvider sessionProvider;

    @Inject
    public SimpleChatDispatcher(ChatFormatService formatService, SessionProvider sessionProvider) {
        this.formatService = formatService;
        this.sessionProvider = sessionProvider;
    }

    @Override
    public SimpleChatDispatcher registerHandler(ChatHandler handler) {
        verifyHandler(handler);
        if (handler.enable()) {
            handlerMap.put(handler.getPhase(), handler);
        }
        return this;
    }

    private void verifyHandler(ChatHandler handler) {
        Preconditions.checkNotNull(handler, "handler");
        Preconditions.checkNotNull(handler.getPhase(), "handler.getPhase()");
    }

    @Override
    public void unregisterHandler(ChatHandler handler) {
        verifyHandler(handler);
        handlerMap.remove(handler.getPhase(), handler);
    }

    @Override
    public void dispatchEvent(AsyncPlayerChatEvent bukkitEvent) {
        sessionProvider.inSession(ignored -> {
            ChatMessageEvent event = createEvent(bukkitEvent);
            dispatchAllPhases(event);
            if (event.isCancelled()) {
                return;
            }
            formatService.sendToAll(event.getPlayer(), event.getMessage());
            notifyIfOnlyPlayer(event);
        });
    }

    private void notifyIfOnlyPlayer(ChatMessageEvent event) {
        Player player = event.getPlayer();
        if (player.getServer().getOnlinePlayers().size() <= 1) {
            I18n.sendLoc(player, Format.warning("core!chat.nobody-hears"));
        }
    }

    private BukkitChatMessageEvent createEvent(AsyncPlayerChatEvent bukkitEvent) {
        return new BukkitChatMessageEvent(bukkitEvent);
    }

    private void dispatchAllPhases(ChatMessageEvent event) {
        for (ChatPhase phase : ChatPhase.values()) {
            dispatchPhase(event, phase);
            if (!event.shouldContinueHandling()) {
                return;
            }
        }
    }

    private void dispatchPhase(ChatMessageEvent event, ChatPhase phase) {
        List<ChatHandler> handlers = handlerMap.get(phase);
        for (ChatHandler handler : handlers) {
            if (!event.shouldContinueHandling()) {
                return;
            }
            dispatchToHandler(event, handler);
        }
    }

    private void dispatchToHandler(ChatMessageEvent event, ChatHandler handler) {
        try {
            handler.handle(event);
        } catch (Exception e) {
            handleHandlerException(event, handler, e);
        }
    }

    private void handleHandlerException(ChatMessageEvent event, ChatHandler handler, Exception e) {
        LOGGER.warn("Handler {} failed to handle event {}: ", handler, event);
        LOGGER.warn("Exception: ", e);
    }
}
