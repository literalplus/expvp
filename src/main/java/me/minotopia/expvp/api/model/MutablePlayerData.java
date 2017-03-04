/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.model;

import me.minotopia.expvp.skill.meta.Skill;

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

    /**
     * Registers with the entity that the player has died, causing the death count to be increased.
     */
    void addDeath();

    /**
     * Causes the player's stats, that is, kill and death count, to be reset to their initial value
     * of zero.
     */
    void clearStats();

    /**
     * Sets the current level of this player. The initial level is 1. This also resets the points
     * count for this player. Note that this does not change the amount of books.
     *
     * @param level the new level of this player
     */
    void setLevel(int level);

    /**
     * Causes this player's level to be increased by one and the points count to be reset. Also adds
     * a book to the player's bookshelf.
     *
     * @return the new level of the player
     */
    int levelUp();

    /**
     * Sets the amount of {@link #getPoints() points} this player has.
     *
     * @param points the new amount of points for this player
     */
    void setPoints(int points);

    /**
     * Sets the current amount of {@link #getBooks() books} this player has.
     *
     * @param books the new amount of books
     */
    void setBooks(int books);

    /**
     * Sets the current amount of {@link #getMelons() melons} this player has.
     *
     * @param melons the new amount of melons
     */
    void setMelons(int melons);

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
}
