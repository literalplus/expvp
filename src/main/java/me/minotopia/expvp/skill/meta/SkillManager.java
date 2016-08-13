/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.meta;

import me.minotopia.expvp.yaml.AbstractYamlManager;

import java.io.File;

/**
 * Manages skill metadata instances, keeping a list of all known ones by id and delegating
 * loading and saving.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class SkillManager extends AbstractYamlManager<Skill> {

    /**
     * Creates a new skill manager.
     *
     * @param skillDirectory the directory to load skills from
     */
    public SkillManager(File skillDirectory) {
        super(skillDirectory);
        loadAll();
    }

    @Override
    protected SkillLoader createLoader() {
        return new SkillLoader(this);
    }
}
