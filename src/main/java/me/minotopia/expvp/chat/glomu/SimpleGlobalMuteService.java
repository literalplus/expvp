/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.chat.glomu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.chat.GlobalMuteService;

import java.util.Optional;

/**
 * A simple global mute service.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-07
 */
@Singleton
public class SimpleGlobalMuteService implements GlobalMuteService {
    private boolean enabled = false;
    private String reason = null;

    @Inject
    public SimpleGlobalMuteService() {

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Optional<String> getReason() {
        return Optional.ofNullable(reason);
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
