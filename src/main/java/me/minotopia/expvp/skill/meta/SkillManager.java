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

package me.minotopia.expvp.skill.meta;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.inject.DataFolder;
import me.minotopia.expvp.yaml.AbstractYamlManager;

import java.io.File;

/**
 * Manages skill metadata instances, keeping a list of all known ones by id and delegating loading
 * and saving.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
@Singleton
public class SkillManager extends AbstractYamlManager<Skill> {
    @Inject
    public SkillManager(@DataFolder File dataFolder) {
        super(new File(dataFolder, "skills"));
        loadAll();
    }

    @Override
    protected SkillLoader createLoader() {
        return new SkillLoader(this);
    }
}
