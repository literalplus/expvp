/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.respawn;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.api.respawn.RespawnService;

/**
 * Provides bindings for everything related to respawning.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-06
 */
public class RespawnModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RespawnService.class).to(SpectatorRespawnService.class);
    }
}
