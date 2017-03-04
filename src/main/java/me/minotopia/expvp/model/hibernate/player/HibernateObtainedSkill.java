/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.hibernate.player;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.model.hibernate.BaseEntity;
import me.minotopia.expvp.skill.meta.Skill;

import javax.persistence21.Column;
import javax.persistence21.Entity;
import javax.persistence21.Id;
import javax.persistence21.JoinColumn;
import javax.persistence21.ManyToOne;
import javax.persistence21.Table;

/**
 * Model for storing data about an actual skill that a player has obtained using books.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-22
 */
@Entity
@Table(name = "exp_player_skill", schema = "mt_main")
public class HibernateObtainedSkill extends BaseEntity implements ObtainedSkill {
    @Id
    @ManyToOne
    @JoinColumn(name = "player_uuid")
    private HibernatePlayerData playerData;

    @Id
    @Column(name = "skill")
    private String skillId;

    @SuppressWarnings("unused")
    HibernateObtainedSkill() {
        //Default constructor required for Hibernate - may be package+
    }

    /**
     * Business constructor for creating obtained skill data.
     *
     * @param playerData the player data
     * @param skillId    the unique identifier of the skill
     */
    public HibernateObtainedSkill(HibernatePlayerData playerData, String skillId) {
        this.playerData = playerData;
        this.skillId = skillId;
    }

    @Override
    public HibernatePlayerData getPlayerData() {
        return playerData;
    }

    @Override
    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean matches(Skill skill) {
        Preconditions.checkNotNull(skill, "skill");
        Preconditions.checkNotNull(skill.getId(), "skill.getId()");
        return skill.getId().equals(skillId);
    }
}
