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
