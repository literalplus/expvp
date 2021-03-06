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

import com.google.common.base.Preconditions;
import li.l1t.common.util.config.YamlHelper;
import me.minotopia.expvp.yaml.AbstractYamlLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.File;
import java.io.IOException;

/**
 * Handles loading and saving of skills.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
public class SkillLoader extends AbstractYamlLoader<Skill> {
    public static String FILE_EXTENSION = ".skill.yml";
    private final SkillManager manager;

    SkillLoader(SkillManager manager) {
        super(manager);
        this.manager = manager;
        ConfigurationSerialization.registerClass(Skill.class);
    }

    @Override
    public Skill loadFromFile(File file) throws IOException, InvalidConfigurationException {
        checkDoesNotExistInManager(getObjectId(file));
        YamlConfiguration config = YamlHelper.load(file, true);
        Skill skill = (Skill) config.get(DATA_PATH);
        Preconditions.checkNotNull(skill, "skill ;; ", config.saveToString());
        skill.setManager(manager);
        return skill;
    }

    @Override
    public void saveToFile(Skill skill) throws IOException {
        File file = getFile(skill.getId());
        YamlConfiguration config = new YamlConfiguration();
        config.set(DATA_PATH, skill);
        config.save(file);
    }

    @Override
    public Skill create(String id) throws IOException {
        checkDoesNotExistInManager(id);
        Skill skill = new Skill(id);
        skill.setManager(manager);
        saveToFile(skill);
        return skill;
    }

    @Override
    public String getFileExtension() {
        return FILE_EXTENSION;
    }

}
