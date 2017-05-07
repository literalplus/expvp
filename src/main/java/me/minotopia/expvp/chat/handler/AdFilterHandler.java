/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.chat.handler;

import li.l1t.common.chat.AdFilterService;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.chat.ChatFormatService;
import me.minotopia.expvp.api.chat.ChatMessageEvent;
import me.minotopia.expvp.api.chat.ChatPhase;
import me.minotopia.expvp.i18n.I18n;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Server;

/**
 * Filters out messages that are probably ads.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-21
 */
public class AdFilterHandler extends AbstractChatHandler {
    private final AdFilterService filterService;
    private final ChatFormatService chatFormatService;
    private final Server server;
    private int mockMessageCount;

    public AdFilterHandler(AdFilterService filterService, ChatFormatService chatFormatService, Server server, int mockMessageCount) {
        super(ChatPhase.CENSORING);
        this.filterService = filterService;
        this.chatFormatService = chatFormatService;
        this.server = server;
        this.mockMessageCount = mockMessageCount;
    }

    @Override
    public void handle(ChatMessageEvent evt) {
        if (filterService.test(evt.getInitialMessage())) {
            handleMatch(evt);
        }
    }

    private void handleMatch(ChatMessageEvent evt) {
        if (!evt.tryDenyMessage(Message.of("chat!ad.sender-response"), this)) {
            return;
        }
        chatFormatService.sendToAll(evt.getPlayer(), getRandomMockMessage());
        broadcastAdInfo(Message.of("chat!ad.admin-info", evt.getPlayer().getName(), evt.getInitialMessage()));
    }

    private Message getRandomMockMessage() {
        int selectedMessage = RandomUtils.nextInt(0, mockMessageCount);
        return Message.of("chat!ad.mock-" + selectedMessage);
    }

    private void broadcastAdInfo(Message message) {
        server.getOnlinePlayers().stream()
                .filter(Permission.CHAT_AD_INFO::has)
                .forEach(player -> I18n.sendLoc(player, message));
    }
}
