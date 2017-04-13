/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.meta;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.inject.DataFolder;
import me.minotopia.expvp.api.service.ResearchService;
import me.minotopia.expvp.yaml.AbstractYamlManager;

import java.io.File;

/**
 * Manages skill metadata instances, keeping a list of all known ones by id and delegating loading
 * and saving.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
@Singleton
public class SkillManager extends AbstractYamlManager<Skill> {
    private final ResearchService obtainmentService;
    private final DisplayNameService names;

    @Inject
    public SkillManager(@DataFolder File dataFolder, ResearchService obtainmentService, DisplayNameService names) {
        super(new File(dataFolder, "skills"));
        this.obtainmentService = Preconditions.checkNotNull(obtainmentService, "obtainmentService");
        this.names = names;
        loadAll();
    }

    @Override
    protected SkillLoader createLoader() {
        return new SkillLoader(this);
    }
}
