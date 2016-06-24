/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.xxyy.common.util.config.YamlHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Loads skill trees from their files and discovers skill trees from a directory.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
class SkillTreeLoader {
    private static Logger LOGGER = LoggingManager.getLogger(SkillTreeLoader.class);
    public static String FILE_EXTENSION = ".tree.yml";
    private final SkillTreeManager manager;

    SkillTreeLoader(SkillTreeManager manager) {
        this.manager = manager;
    }

    SkillTree loadFromFile(File file) throws IOException, InvalidConfigurationException {
        checkExists(getTreeId(file));
        YamlConfiguration config = YamlHelper.load(file, true);
        return new SkillTree(config.getValues(true));
    }

    Set<SkillTree> loadFromDirectory() {
        Set<SkillTree> trees = new HashSet<>();
        for (File file : manager.getDirectory().listFiles(f -> f.getName().endsWith(FILE_EXTENSION))) {
            try {
                trees.add(loadFromFile(file));
            } catch (Exception e) { //Make sure we can still load the other trees at least
                LOGGER.warn("Failed to load skill tree " + file.getAbsolutePath() + ": ", e);
                e.printStackTrace();
            }
        }
        return trees;
    }

    /**
     * Saves a whole tree to the file specified by the tree id.
     *
     * @param tree the tree to save
     * @throws IOException if an error occurs writing the file
     */
    void saveToFile(SkillTreeNode tree) throws IOException {
        File file = getFile(tree.getTreeId());
        YamlConfiguration config = new YamlConfiguration();
        config.set("", tree.serialize());
        config.save(file);
    }

    SkillTree createTree(String id) throws IOException {
        checkExists(id);
        SkillTree tree = new SkillTree(id);
        saveToFile(tree);
        return tree;
    }

    String getTreeId(File file) {
        return file.getName().replace(FILE_EXTENSION, "");
    }

    private File getFile(String treeId) {
        return new File(manager.getDirectory(), treeId + FILE_EXTENSION);
    }

    private void checkExists(String treeId) {
        Preconditions.checkArgument(manager.hasTree(treeId),
                "a tree already exists with that id: %s", treeId);
    }
}
