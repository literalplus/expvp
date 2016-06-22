/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.player;

import me.minotopia.expvp.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Model for storing data about an actual skill that a player has obtained using books.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-22
 */
@Entity
@Table(name = "exp_player_skill")
public class ObtainedSkill extends BaseEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "player_uuid")
    private PlayerData playerData;

    @Id
    @Column(name = "tree")
    private String skillTree;

    @Id
    @Column(name = "skill")
    private String skillId;

    @SuppressWarnings("unused")
    ObtainedSkill() {
        //Default constructor required for Hibernate - may be package+
    }

    /**
     * Business constructor for creating obtained skill data.
     * @param playerData the player data
     * @param skillTree the skill tree this skill is from
     * @param skillId the tree-unique identifier of the skill
     */
    public ObtainedSkill(PlayerData playerData, String skillTree, String skillId) {
        this.playerData = playerData;
        this.skillTree = skillTree;
        this.skillId = skillId;
    }

    /**
     * @return the player who has this skill
     */
    public PlayerData getPlayerData() {
        return playerData;
    }


    /**
     * @return the skill tree this skill is from
     */
    public String getSkillTree() {
        return skillTree;
    }

    /**
     * Sets the skill tree this skill is from.
     * @param skillTree the new skill tree this skill is from
     */
    public void setSkillTree(String skillTree) {
        this.skillTree = skillTree;
    }

    /**
     * @return the the tree-unique identifier of the skill
     */
    public String getSkillId() {
        return skillId;
    }

    /**
     * Sets the tree-unique identifier of the skill.
     * @param skillId the tree-unique identifier of the skill
     */
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
}
