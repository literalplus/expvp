/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
