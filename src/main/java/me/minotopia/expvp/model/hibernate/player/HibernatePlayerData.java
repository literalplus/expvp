/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.hibernate.player;

import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.model.hibernate.BaseEntity;

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
 * Stores player data related to Expvp.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-21
 */
@Entity
@Table(name = "mt_main.exp_player")
public class HibernatePlayerData extends BaseEntity implements MutablePlayerData {
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
    HibernatePlayerData() {
        //Default constructor required for Hibernate - may be package+
    }

    /**
     * Creates a new player data instance for a new player with all values set to their defaults.
     *
     * @param uuid the unique id of the player
     */
    public HibernatePlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public void addKill() {
        this.kills += 1;
    }

    @Override
    public int getDeaths() {
        return deaths;
    }

    @Override
    public void addDeath() {
        this.deaths += 1;
    }

    @Override
    public void clearStats() {
        this.kills = 0;
        this.deaths = 0;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
        this.points = 0;
    }

    @Override
    public int levelUp() {
        setLevel(level + 1);
        setBooks(melons + 1); //TODO: book limits
        return getLevel();
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int getBooks() {
        return books;
    }

    @Override
    public void setBooks(int books) {
        this.books = books;
    }

    @Override
    public int getMelons() {
        return melons;
    }

    @Override
    public void setMelons(int melons) {
        this.melons = melons;
    }

    @Override
    public Set<ObtainedSkill> getSkills() {
        return skillsView;
    }

    @Override
    public void addSkill(ObtainedSkill newSkill) {
        skills.add(newSkill);
    }

    @Override
    public void removeSkill(ObtainedSkill oldSkill) {
        skills.remove(oldSkill);
    }

    @Override
    public void clearSkills() {
        skills.clear();
    }

    //Implement equals/hashCode if this entity persists across multiple Sessions and has generated ids
    // See docs, 2.5.7 - https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html
}
