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
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.model.PlayerPoints;
import me.minotopia.expvp.api.score.points.TalentPointType;
import me.minotopia.expvp.model.hibernate.converter.TalentPointTypeConverter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence21.*;
import java.io.Serializable;


/**
 * Stores the amount of Talent Points a player has of a type in total using Hibernate.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-01
 */
@Entity
@Table(name = "exp_player_point_type", schema = "mt_main")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HibernatePoints implements PlayerPoints, Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "player_uuid")
    private HibernatePlayerData playerData;

    @Id
    @Column(name = "pointtype")
    @Convert(converter = TalentPointTypeConverter.class)
    private TalentPointType type;

    @Column(name = "points")
    private int currentPointCount;


    public HibernatePoints() {
        //Default constructor required for Hibernate - may be package+
    }

    public HibernatePoints(HibernatePlayerData playerData, TalentPointType type) {
        this.playerData = Preconditions.checkNotNull(playerData, "playerData");
        this.type = Preconditions.checkNotNull(type, "type");
    }

    @Override
    public PlayerData getPlayerData() {
        return playerData;
    }

    @Override
    public TalentPointType getType() {
        return type;
    }

    @Override
    public int getCurrentPointCount() {
        return currentPointCount;
    }

    @Override
    public void increasePointCountBy(int modifier) {
        Preconditions.checkArgument(modifier >= 0, "modifier may not be negative: ", modifier);
        currentPointCount += modifier;
    }
}
