/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.minotopia.expvp.model.hibernate.player;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.model.hibernate.BaseEntity;
import me.minotopia.expvp.skill.meta.Skill;

import javax.persistence21.*;

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
