/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.player;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.model.BaseEntity;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skill.tree.SimpleSkillTreeNode;

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
    @Column(name = "skill")
    private String skillId;

    @SuppressWarnings("unused")
    ObtainedSkill() {
        //Default constructor required for Hibernate - may be package+
    }

    /**
     * Business constructor for creating obtained skill data.
     *
     * @param playerData the player data
     * @param skillId    the unique identifier of the skill
     */
    public ObtainedSkill(PlayerData playerData, String skillId) {
        this.playerData = playerData;
        this.skillId = skillId;
    }

    /**
     * @return the player who has this skill
     */
    public PlayerData getPlayerData() {
        return playerData;
    }

    /**
     * @return the the unique identifier of the skill
     */
    public String getSkillId() {
        return skillId;
    }

    /**
     * Sets the tree-unique identifier of the skill.
     *
     * @param skillId the tree-unique identifier of the skill
     */
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    /**
     * Checks whether given skill has the same id as this model skill.
     *
     * @param skill the skill to compare to
     * @return whether the ids are the same
     */
    public boolean matches(Skill skill) {
        Preconditions.checkNotNull(skill, "skill");
        Preconditions.checkNotNull(skill.getId(), "skill.getId()");
        return skill.getId().equals(skillId);
    }

    /**
     * Creates a model skill from a skill tree node.
     *
     * @param playerData the player data
     * @param node       the node to convert
     * @return a model skill corresponding to given arguments
     */
    public static ObtainedSkill fromNode(PlayerData playerData, SimpleSkillTreeNode node) {
        Preconditions.checkNotNull(playerData, "playerData");
        Preconditions.checkNotNull(node, "node");
        Preconditions.checkNotNull(node.getValue(), "node.getValue()");
        return new ObtainedSkill(playerData, node.getValue().getId());
    }
}
