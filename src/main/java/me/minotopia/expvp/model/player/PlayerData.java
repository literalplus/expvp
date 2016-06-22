/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.player;

import me.minotopia.expvp.model.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Stores player data related to ExPvP.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-21
 */
@Entity
@Table(name = "mt_main.exp_player")
public class PlayerData extends BaseEntity {
    @Id
    private UUID uuid; //always use Object types for identifiers, they didn't explain why tho
    private int kills; //how many players this player has killed overall
    private int deaths; //how often this player has died overall
    private int level = 1; //current level id (starting at 1)
    private int points; //current amount of points (kills and deaths) - reset at level-up
    private int books; //books, that is, skill points left to allocate
    private int melons; //melons, that is, premium currency for cosmetic stuff (yay EULA!)

    @ManyToOne(cascade = CascadeType.ALL)
    private Set<ObtainedSkill> skills = new HashSet<>();
    @Transient
    private Set<ObtainedSkill> skillsView = Collections.unmodifiableSet(skills);

    @SuppressWarnings("unused")
    PlayerData() {
        //Default constructor required for Hibernate - may be package+
    }

    /**
     * Creates a new player data instance for a new player with all values set to their defaults.
     *
     * @param uuid the unique id of the player
     */
    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the unique id of the player
     */
    public UUID getUniqueId() {
        return uuid;
    }

    /**
     * @return the amount of times this player has killed other players overall
     */
    public int getKills() {
        return kills;
    }

    /**
     * Registers with the entity that the player has killed another player, causing the kill
     * count to be increased.
     */
    public void addKill() {
        this.kills += 1;
    }

    /**
     * @return the amount of times this player has died
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * Registers with the entity that the player has died, causing the death count to be increased.
     */
    public void addDeath() {
        this.deaths += 1;
    }

    /**
     * Causes the player's stats, that is, kill and death count, to be reset to their initial
     * value of zero.
     */
    public void clearStats() {
        this.kills = 0;
        this.deaths = 0;
    }

    /**
     * @return the current level of this player, starting at 1
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the current level of this player. The initial level is 1. This also resets the points
     * count for this player. Note that this does not change the amount of books.
     *
     * @param level the new level of this player
     * @see #levelUp() the correct API way to level up by one
     */
    public void setLevel(int level) {
        this.level = level;
        this.points = 0;
    }

    /**
     * Causes this player's level to be increased by one and the points count to be reset. Also
     * adds a book to the player's bookshelf.
     *
     * @return the new level of the player
     */
    public int levelUp() {
        setLevel(level + 1);
        setBooks(melons + 1); //TODO: book limits
        return getLevel();
    }

    /**
     * Gets the amount of points this player currently has for progressing to the next level.
     * Points are increased each time the player kills somebody and decreased every time the
     * player dies. Upon reaching a certain amount of points, the player progresses to the next
     * level.
     *
     * @return the amount of points the player currently has
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the amount of {@link #getPoints() points} this player has.
     *
     * @param points the new amount of points for this player
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the amount of books this player currently has. Books can be used to obtain skills
     * from a skill tree and are added at level-up, except if the player has reached their book
     * limit.
     * @return the current amount of books this player has
     */
    public int getBooks() {
        return books;
    }

    /**
     * Sets the current amount of {@link #getBooks() books} this player has.
     * @param books the new amount of books
     */
    public void setBooks(int books) {
        this.books = books;
    }

    /**
     * Gets the amount of melons this player has. Melons are a premium currency used for
     * purchasing cosmetic things.
     * @return the current amount of melons this player has
     */
    public int getMelons() {
        return melons;
    }

    /**
     * Sets the current amount of {@link #getMelons() melons} this player has.
     * @param melons the new amount of melons
     */
    public void setMelons(int melons) {
        this.melons = melons;
    }

    /**
     * Gets the set of skills this player has.
     * @return the set of skills
     */
    public Set<ObtainedSkill> getSkills() {
        return skillsView;
    }

    /**
     * Adds a new skill to the player's set of skills.
     * @param newSkill the new skill to add
     */
    public void addSkill(ObtainedSkill newSkill) {
        skills.add(newSkill);
    }

    /**
     * Removes a skill from this player's set of skills.
     * @param oldSkill the skill to remove
     */
    public void removeSkill(ObtainedSkill oldSkill) {
        skills.remove(oldSkill);
    }

    /**
     * Clears this player's set of skills.
     */
    public void clearSkills() {
        skills.clear();
    }

    //Implement equals/hashCode if this entity persists across multiple Sessions and has generated ids
    // See docs, 2.5.7 - https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html
}
