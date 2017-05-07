/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.chat;

import java.util.Optional;

/**
 * Manages global mute state of the chat.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-07
 */
public interface GlobalMuteService {
    boolean isEnabled();

    Optional<String> getReason();

    void setReason(String reason);

    void setEnabled(boolean enabled);
}
