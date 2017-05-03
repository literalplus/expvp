/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.chat.handler;


import me.minotopia.expvp.api.chat.ChatHandler;
import me.minotopia.expvp.api.chat.ChatPhase;

/**
 * Abstract base class for chat handlers.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-21
 */
abstract class AbstractChatHandler implements ChatHandler {
    private final ChatPhase phase;

    protected AbstractChatHandler(ChatPhase phase) {
        this.phase = phase;
    }

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public void disable() {
        //no-op by default
    }

    @Override
    public ChatPhase getPhase() {
        return phase;
    }
}
