/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
