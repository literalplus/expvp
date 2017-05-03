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
import me.minotopia.expvp.api.chat.ChatMessageEvent;
import me.minotopia.expvp.api.chat.ChatPhase;

import java.util.regex.Pattern;

/**
 * Blocks messages claiming there's server lag.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-21
 */
public class LagMessageHandler extends AbstractChatHandler {
    private static final Pattern LAG_PATTERN = Pattern.compile("\\b([lL]+[aA4]+[gG]+)\\b");

    @Inject
    public LagMessageHandler() {
        super(ChatPhase.FILTERING);
    }

    @Override
    public void handle(ChatMessageEvent evt) {
        if (!containsLagMessage(evt)) {
            return;
        }

        evt.tryDenyMessage(Message.of("chat!lag.warn"), this);
    }

    private boolean containsLagMessage(ChatMessageEvent evt) {
        return LAG_PATTERN.matcher(evt.getMessage()).find();
    }
}
