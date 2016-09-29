/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.service;

import li.l1t.common.exception.UserException;
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
    /**
     * Attempts to add an obtained skill to a player.
     *
     * @param playerId the unique id of the player
     * @param skill    the skill to obtain
     * @throws UserException if the skill is already obtained or something else prevents obtainment
     */
    void addObtainedSkill(UUID playerId, Skill skill);

    /**
     * Attempts to remove an obtained skill from a player.
     *
     * @param playerId the unique id of the player
     * @param skill    the skill to forget
     * @throws UserException if the skill has not been obtained
     */
    void removeObtainedSkill(UUID playerId, Skill skill);

    /**
     * Checks whether a player has obtained a skill.
     *
     * @param playerId the unique id of the player
     * @param skill    the skill to check
     * @return whether given player has obtained given skill
     */
    boolean hasObtainedSkill(UUID playerId, Skill skill);

    /**
     * @param playerId the unique id of the player
     * @return the collection of skill ids given player has obtained
     */
    Collection<String> getObtainedSkills(UUID playerId);
}
