/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
