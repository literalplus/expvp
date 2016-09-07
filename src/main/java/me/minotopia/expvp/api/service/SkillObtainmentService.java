/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.service;

import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;
import java.util.UUID;

/**
 * A service that provides read/write access to skill metadata of players.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-08
 */
public interface SkillObtainmentService {
    void addObtainedSkill(UUID playerId, Skill skill);

    void removeObtainedSkill(UUID playerId, Skill skill);

    boolean hasObtainedSkill(UUID playerId, Skill skill);

    Collection<String> getObtainedSkills(UUID playerId);
}
