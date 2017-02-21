/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.model;

import me.minotopia.expvp.skill.meta.Skill;

/**
 * Represents a skill that has already been obtained by a player. This is tied to a single player
 * and to a single skill.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-08
 */
public interface ObtainedSkill {
    /**
     * @return the player who has this skill
     */
    PlayerData getPlayerData();

    /**
     * @return the the unique identifier of the skill
     */
    String getSkillId();

    /**
     * Checks whether given skill has the same id as this model skill.
     *
     * @param skill the skill to compare to
     * @return whether the ids are the same
     */
    boolean matches(Skill skill);
}
