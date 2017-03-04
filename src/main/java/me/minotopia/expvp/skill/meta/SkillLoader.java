/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.meta;

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
