/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.spawn;

import li.l1t.common.util.config.YamlHelper;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.yaml.AbstractYamlLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.File;
import java.io.IOException;

/**
 * Handles loading and saving of map spawns.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-12
 */
public class SpawnLoader extends AbstractYamlLoader<MapSpawn> {
    public static String FILE_EXTENSION = ".spawn.yml";

    SpawnLoader(SpawnManager manager) {
        super(manager);
        ConfigurationSerialization.registerClass(Skill.class);
    }

    @Override
    public MapSpawn loadFromFile(File file) throws IOException, InvalidConfigurationException {
        checkDoesNotExistInManager(getObjectId(file));
        YamlConfiguration config = YamlHelper.load(file, true);
        return (MapSpawn) config.get(DATA_PATH);
    }

    @Override
    public void saveToFile(MapSpawn spawn) throws IOException {
        File file = getFile(spawn.getId());
        YamlConfiguration config = new YamlConfiguration();
        config.set(DATA_PATH, spawn);
        config.save(file);
    }

    @Override
    public MapSpawn create(String id) throws IOException {
        checkDoesNotExistInManager(id);
        MapSpawn spawn = new YamlMapSpawn(id);
        saveToFile(spawn);
        return spawn;
    }

    @Override
    public String getFileExtension() {
        return FILE_EXTENSION;
    }

}
