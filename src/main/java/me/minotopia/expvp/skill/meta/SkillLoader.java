/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.meta;

import me.minotopia.expvp.yaml.AbstractYamlLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.xxyy.common.util.config.YamlHelper;

import java.io.File;
import java.io.IOException;

/**
 * Handles loading and saving of skills.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class SkillLoader extends AbstractYamlLoader<Skill> {
    public static String FILE_EXTENSION = ".skill.yml";

    SkillLoader(SkillManager manager) {
        super(manager);
    }

    @Override
    public Skill loadFromFile(File file) throws IOException, InvalidConfigurationException {
        checkExists(getObjectId(file));
        YamlConfiguration config = YamlHelper.load(file, true);
        return (Skill) config.get(DATA_PATH);
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
        checkExists(id);
        Skill skill = new Skill(id);
        saveToFile(skill);
        return skill;
    }

    @Override
    public String getFileExtension() {
        return FILE_EXTENSION;
    }

}
