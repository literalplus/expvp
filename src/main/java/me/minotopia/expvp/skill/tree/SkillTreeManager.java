/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages in-memory skill trees and delegates loading.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
public class SkillTreeManager {
    private final File directory;
    private final SkillTreeLoader loader;
    private final Map<String, SkillTree> trees = new HashMap<>();
    private final Collection<SkillTree> treesView = Collections.unmodifiableCollection(trees.values());

    /**
     * Creates a new skill tree manager for a given directory.
     * @param directory the directory where skill trees are stored
     */
    public SkillTreeManager(File directory) {
        this.directory = directory;
        this.loader = new SkillTreeLoader(this);
        loadTrees();
    }

    /**
     * Loads all trees from disk.
     */
    public void loadTrees() {
        trees.clear();
        loader.loadFromDirectory()
                .forEach(tree -> trees.put(tree.getId(), tree));
    }

    /**
     * @return the directory where skill trees are stored
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * @return the skill tree loader associated with this manager
     */
    SkillTreeLoader getLoader() {
        return loader;
    }

    /**
     * @return an immutable view of the current in-memory collection of trees
     */
    public Collection<SkillTree> getTrees() {
        return treesView;
    }

    /**
     * Gets a tree by its id, if it exists in memory. Otherwise returns null.
     * @param treeId the id of the tree to fetch
     * @return a tree with given id (case-sensitive) or null
     */
    public SkillTree getTree(String treeId) {
        return trees.get(treeId);
    }

    /**
     * Checks whether a tree exists in memory by a given id.
     * @param treeId the id of the tree to seek
     * @return whether a tree exists by the given id
     */
    public boolean hasTree(String treeId) {
        return trees.containsKey(treeId);
    }

    /**
     * Creates a new skill tree and saves it to its file.
     * @param treeId the unique id of the new tree
     * @return the created tree
     * @throws IOException if an error occurs writing the file
     */
    public SkillTree createTree(String treeId) throws IOException {
        SkillTree tree = loader.createTree(treeId);
        trees.put(tree.getId(), tree);
        return tree;
    }

    /**
     * Saves a tree to its file.
     * @param tree the tree to save
     * @throws IOException if an error occurs writing to the file
     */
    public void saveTree(SkillTree tree) throws IOException {
        loader.saveToFile(tree);
    }
}
