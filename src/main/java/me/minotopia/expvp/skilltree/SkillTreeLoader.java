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

package me.minotopia.expvp.skilltree;

import li.l1t.common.util.config.YamlHelper;
import me.minotopia.expvp.yaml.AbstractYamlLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Loads skill trees from their files and discovers skill trees from a directory.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-23
 */
class SkillTreeLoader extends AbstractYamlLoader<SkillTree> {
    public static String FILE_EXTENSION = ".tree.yml";

    SkillTreeLoader(SkillTreeManager manager) {
        super(manager);
    }

    @Override
    public SkillTree loadFromFile(File file) throws IOException, InvalidConfigurationException {
        checkDoesNotExistInManager(getObjectId(file));
        YamlConfiguration config = YamlHelper.load(file, true);
        return new SkillTree(config.getConfigurationSection(DATA_PATH).getValues(true));
    }

    @Override
    public void saveToFile(SkillTree tree) throws IOException {
        File file = getFile(tree.getTreeId());
        YamlConfiguration config = new YamlConfiguration();
        config.set(DATA_PATH, tree.serialize());
        config.save(file);
    }

    @Override
    public SkillTree create(String id) throws IOException {
        checkDoesNotExistInManager(id);
        SkillTree tree = new SkillTree(id);
        saveToFile(tree);
        return tree;
    }

    @Override
    public String getFileExtension() {
        return FILE_EXTENSION;
    }
}
