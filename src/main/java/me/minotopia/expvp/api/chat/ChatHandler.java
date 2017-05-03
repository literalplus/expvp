/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.chat;

/**
 * Handles and transforms chat message events.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-21
 */
public interface ChatHandler {
    void handle(ChatMessageEvent evt);

    boolean enable();

    void disable();

    ChatPhase getPhase();
}
