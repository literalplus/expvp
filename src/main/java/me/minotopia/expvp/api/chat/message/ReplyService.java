/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.chat.message;

import java.util.Optional;
import java.util.UUID;

/**
 * Keeps track of who command senders communicated with most recently for a certain time.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
public interface ReplyService {
    void registerMessage(PrivateMessage message);

    Optional<UUID> getMostRecentPartner(UUID senderId);
}
