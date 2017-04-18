/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.model.player.HibernatePlayerDataService;
import me.minotopia.expvp.model.player.HibernateResetService;

/**
 * Provides the dependency wiring for the model module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-09
 */
public class ModelModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PlayerDataService.class).to(HibernatePlayerDataService.class);
        bind(HibernateResetService.class);
    }
}
