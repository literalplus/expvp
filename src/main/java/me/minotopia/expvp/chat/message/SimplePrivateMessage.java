/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.chat.message;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.chat.message.PrivateMessage;

import java.time.Instant;
import java.util.UUID;

/**
 * A simple private message.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
public class SimplePrivateMessage implements PrivateMessage {
    private final UUID senderId;
    private final UUID receiverId;
    private String message;
    private final Instant instant;

    public SimplePrivateMessage(UUID senderId, UUID receiverId, String message) {
        this.instant = Instant.now();
        this.senderId = Preconditions.checkNotNull(senderId, "senderId");
        this.receiverId = Preconditions.checkNotNull(receiverId, "receiverId");
        this.message = Preconditions.checkNotNull(message, "message");
    }

    @Override
    public UUID getSenderId() {
        return senderId;
    }

    @Override
    public UUID getReceiverId() {
        return receiverId;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Instant getInstant() {
        return instant;
    }
}
