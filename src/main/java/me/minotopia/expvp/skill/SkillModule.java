/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.api.service.SkillObtainmentService;
import me.minotopia.expvp.api.skill.SkillService;
import me.minotopia.expvp.skill.meta.SimpleSkillService;
import me.minotopia.expvp.skill.obtainment.CostCheckingObtainmentService;
import me.minotopia.expvp.skill.obtainment.SimpleSkillObtainmentService;
import me.minotopia.expvp.util.SessionProvider;

/**
 * Provides the dependency wiring for the skill module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-09
 */
public class SkillModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SkillService.class).to(SimpleSkillService.class);
    }

    @Provides
    SkillObtainmentService skillObtainmentService(PlayerDataService playerDataService, SessionProvider sessionProvider) {
        return new CostCheckingObtainmentService(
                new SimpleSkillObtainmentService(playerDataService),
                playerDataService,
                sessionProvider
        );
    }
}
