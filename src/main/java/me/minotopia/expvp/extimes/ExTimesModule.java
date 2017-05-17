/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.extimes;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.api.extimes.ExTimesConfig;
import me.minotopia.expvp.api.extimes.ExTimesService;
import me.minotopia.expvp.extimes.cfg.YamlExTimesConfig;

/**
 * Provides dependency wiring for the ExTimes modules.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
public class ExTimesModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ExTimesConfig.class).to(YamlExTimesConfig.class);
        bind(YamlExTimesConfig.class);
        bind(ExTimesService.class).to(YamlExTimesService.class);
        bind(ExTimesJoinListener.class);
    }
}
