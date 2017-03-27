/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.api.score.ExpService;

/**
 * Injector module for everything related to scoring, including Exp, Talent Points, and Leagues.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
public class ScoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ExpService.class).to(PlayerDataExpService.class);
    }
}
