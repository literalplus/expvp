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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.chat.message.PrivateMessage;
import me.minotopia.expvp.api.chat.message.ReplyService;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Remembers last received and sent message per player in a map.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
@Singleton
public class MapReplyService implements ReplyService {
    private final Map<UUID, PrivateMessage> mostRecentMessages = new HashMap<>();

    @Inject
    public MapReplyService(TaskService tasks) {
        tasks.repeating(this::expireCache, Duration.ofMinutes(5));
    }

    @Override
    public void registerMessage(PrivateMessage message) {
        mostRecentMessages.put(message.getSenderId(), message);
        mostRecentMessages.put(message.getReceiverId(), message);
    }

    @Override
    public Optional<UUID> getMostRecentPartner(UUID senderId) {
        return Optional.ofNullable(mostRecentMessages.get(senderId))
                .map(msg -> getOther(msg, senderId));
    }

    private UUID getOther(PrivateMessage message, UUID ownId) {
        if (message.getSenderId().equals(ownId)) {
            return message.getReceiverId();
        } else {
            return message.getSenderId();
        }
    }

    private void expireCache() {
        mostRecentMessages.values().removeIf(
                msg -> msg.getInstant()
                        .plusSeconds(TimeUnit.SECONDS.convert(10, TimeUnit.MINUTES))
                        .isBefore(Instant.now())
        );
    }
}
