/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.api.service.ResearchService;
import me.minotopia.expvp.api.skill.SkillService;
import me.minotopia.expvp.skill.meta.SimpleSkillService;
import me.minotopia.expvp.skill.meta.SkillManager;
import me.minotopia.expvp.skill.obtainment.TalentPointResearchService;

/**
 * Provides the dependency wiring for the skill module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-09
 */
public class SkillModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SkillManager.class);
        bind(SkillService.class).to(SimpleSkillService.class);
        bind(ResearchService.class).to(TalentPointResearchService.class);
    }
}
