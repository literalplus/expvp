/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.hibernate.player;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.api.score.points.InsufficientTalentPointsException;
import me.minotopia.expvp.api.score.points.TalentPointType;
import me.minotopia.expvp.model.hibernate.BaseEntity;
import me.minotopia.expvp.model.hibernate.converter.LocaleConverter;
import me.minotopia.expvp.skill.meta.Skill;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence21.*;
import java.util.*;

/**
 * Stores player data related to Expvp.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-21
 */
@Entity
@Table(name = "exp_player", schema = "mt_main")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HibernatePlayerData extends BaseEntity implements MutablePlayerData {

    @Id
    private UUID uuid; //always use Object types for identifiers, they didn't explain why tho
    @Column(name = "totalkills")
    private int totalKills;
    @Column(name = "totaldeaths")
    private int totalDeaths;
    @Column(name = "totalassists")
    private int totalKillAssists;
    @Column(name = "currentkills")
    private int currentKills;
    @Column(name = "currentdeaths")
    private int currentDeaths;
    @Column(name = "league")
    private String leagueName;
    private int exp;
    @Column(name = "talentpoints")
    private int talentPoints;
    @Convert(converter = LocaleConverter.class)
    private Locale locale;
    @Column(name = "localechanged")
    private boolean customLocale;
    @Column(name = "bestkillstreak")
    private int bestKillStreak;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = HibernateObtainedSkill.class, mappedBy = "playerData")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ObtainedSkill> skills = new HashSet<>();

    @ElementCollection
    @MapKey(name = "type")
    @MapKeyEnumerated(EnumType.ORDINAL)
    @OneToMany(cascade = CascadeType.ALL, targetEntity = HibernatePoints.class, mappedBy = "playerData")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Map<TalentPointType, HibernatePoints> points = new HashMap<>();

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
        this.locale = Locale.getDefault();
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public int getTotalKills() {
        return totalKills;
    }

    @Override
    public int getCurrentKills() {
        return currentKills;
    }

    @Override
    public void addKill() {
        this.totalKills += 1;
        this.currentKills += 1;
    }

    @Override
    public int getTotalKillAssists() {
        return totalKillAssists;
    }

    @Override
    public void addKillAssist() {
        this.totalKillAssists += 1;
    }

    @Override
    public int getTotalDeaths() {
        return totalDeaths;
    }

    @Override
    public int getCurrentDeaths() {
        return currentDeaths;
    }

    @Override
    public void addDeath() {
        this.totalDeaths += 1;
        this.currentDeaths += 1;
    }

    @Override
    public String getLeagueName() {
        return leagueName;
    }

    @Override
    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    @Override
    public int getExp() {
        return exp;
    }

    @Override
    public void setExp(int exp) {
        this.exp = exp;
    }

    @Override
    public int getAvailableTalentPoints() {
        return talentPoints;
    }

    @Override
    @Deprecated
    public void setTalentPoints(int talentPoints) {
        this.talentPoints = talentPoints;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public boolean hasCustomLocale() {
        return customLocale;
    }

    public void setCustomLocale(boolean customLocale) {
        this.customLocale = customLocale;
    }

    @Override
    public int getBestKillStreak() {
        return bestKillStreak;
    }

    @Override
    public void setBestKillStreak(int bestKillStreak) {
        this.bestKillStreak = bestKillStreak;
    }

    @Override
    public Set<ObtainedSkill> getSkills() {
        return skills;
    }

    @Override
    public void addSkill(Skill newSkill) {
        skills.add(new HibernateObtainedSkill(this, newSkill.getId()));
    }

    @Override
    public void removeSkill(Skill oldSkill) {
        skills.removeIf(oldSkill::matches);
    }

    @Override
    public void clearSkills() {
        skills.clear();
    }

    @Override
    public int getTalentPointCount(TalentPointType type) {
        return Optional.ofNullable(points.get(type))
                .map(HibernatePoints::getCurrentPointCount)
                .orElse(0);
    }

    @Override
    public void grantTalentPoints(TalentPointType type, int amount) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkArgument(amount >= 0, "amount may not be negative:", amount);
        HibernatePoints pointsObj = points.computeIfAbsent(type, type0 -> new HibernatePoints(this, type0));
        pointsObj.increasePointCountBy(amount);
        talentPoints += amount;
    }

    @Override
    public void consumeTalentPoints(int amount) throws InsufficientTalentPointsException {
        Preconditions.checkArgument(amount >= 0, "amount may not be negative:", amount);
        if (talentPoints < amount) {
            throw new InsufficientTalentPointsException(amount, talentPoints);
        }
        talentPoints -= amount;
    }

    //Implement equals/hashCode if this entity persists across multiple Sessions and has generated ids
    // See docs, 2.5.7 - https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HibernatePlayerData that = (HibernatePlayerData) o;

        return uuid.equals(that.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
