/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skilltree;

import me.minotopia.expvp.skill.meta.SkillManager;
import me.minotopia.expvp.yaml.AbstractYamlManager;
import me.minotopia.expvp.yaml.YamlLoader;

import java.io.File;

/**
 * Manages in-memory skill trees and delegates loading.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-23
 */
public class SkillTreeManager extends AbstractYamlManager<SkillTree> {

    private final SkillManager skillManager;

    /**
     * Creates a new skill tree manager for a given directory.
     *
     * @param directory the directory where skill trees are stored
     */
    public SkillTreeManager(File directory, SkillManager skillManager) {
        super(directory);
        this.skillManager = skillManager;
        loadAll();
    }

    /**
     * {@inheritDoc} <p>Also {@link #populate() populates} all loaded trees with skill
     * metadata.</p>
     */
    @Override
    public void loadAll() {
        super.loadAll();
        populate();
    }

    @Override
    protected YamlLoader<SkillTree> createLoader() {
        return new SkillTreeLoader(this);
    }

    /**
     * Populates all loaded skill trees with skill metadata from the skill manager associated with
     * this skill tree manager.
     */
    public void populate() {
        getAll().forEach(tree ->
                tree.forEachNode(node ->
                        node.setValue(skillManager.get(node.getSkillId()) //null if not found
                        )
                ));
    }
}
