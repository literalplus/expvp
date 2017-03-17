/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.meta;

import com.google.inject.Inject;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.skill.SkillService;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides skill instances.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public class SimpleSkillService implements SkillService {
    private final SkillManager skillManager;

    @Inject
    public SimpleSkillService(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @Override
    public Collection<Skill> getSkills(PlayerData playerData) {
        return playerData.getSkills().stream()
                .map(ObtainedSkill::getSkillId)
                .map(skillManager::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
