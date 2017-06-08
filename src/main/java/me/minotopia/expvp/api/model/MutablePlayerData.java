/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.model;

import me.minotopia.expvp.api.score.points.InsufficientTalentPointsException;
import me.minotopia.expvp.api.score.points.TalentPointType;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.Locale;

/**
 * Represents a player's data related to Expvp, providing write access to certain properties.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-08
 */
public interface MutablePlayerData extends PlayerData {
    /**
     * Registers with the entity that the player has killed another player, causing the kill count
     * to be increased.
     */
    void addKill();

    void addKillAssist();

    /**
     * Registers with the entity that the player has died, causing the death count to be increased.
     */
    void addDeath();

    /**
     * Sets the league name of this player.
     *
     * @param leagueName the name of the new league of this player
     */
    void setLeagueName(String leagueName);

    /**
     * Sets the amount of {@link #getExp() Exp} this player has.
     *
     * @param exp the new amount of Exp for this player
     */
    void setExp(int exp);

    /**
     * Sets the current amount of {@link #getAvailableTalentPoints() talent points} this player has.
     *
     * @param talentPoints the new amount of talent points
     * @deprecated Prefer {@link #grantTalentPoints(TalentPointType, int)}, which respects limits.
     */
    void setTalentPoints(int talentPoints);

    /**
     * @param locale the new locale for this player
     */
    void setLocale(Locale locale);

    /**
     * @param customLocale whether the player has {@link #hasCustomLocale() changed their locale}
     */
    void setCustomLocale(boolean customLocale);

    void setBestKillStreak(int bestKillStreak);

    /**
     * Adds a new skill to the player's set of skills.
     *
     * @param newSkill the new skill to add
     */
    void addSkill(Skill newSkill);

    /**
     * Removes a skill from this player's set of skills.
     *
     * @param oldSkill the skill to remove
     */
    void removeSkill(Skill oldSkill);

    /**
     * Clears this player's set of skills.
     */
    void clearSkills();

    /**
     * Grants this player given amount of Talent Points of given type.
     *
     * @param type   the type to grant the Talent Points for
     * @param amount the amount of Talent Points to grant
     */
    void grantTalentPoints(TalentPointType type, int amount);

    /**
     * Consumes given amount of Talent Points from this player's available Talent Points.
     *
     * @param amount the amount to consume
     * @throws InsufficientTalentPointsException if this player would have a negative amount of Talent Points after this
     *                                           operation
     */
    void consumeTalentPoints(int amount) throws InsufficientTalentPointsException;
}
