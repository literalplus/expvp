/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
