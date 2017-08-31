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
