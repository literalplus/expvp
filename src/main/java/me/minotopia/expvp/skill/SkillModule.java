/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
