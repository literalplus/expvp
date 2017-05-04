/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.chat.message;

import java.time.Instant;
import java.util.UUID;

/**
 * A private message between two command senders.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
public interface PrivateMessage {
    UUID getSenderId();

    UUID getReceiverId();

    Instant getInstant();

    String getMessage();

    void setMessage(String newMessage);
}
