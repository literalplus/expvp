/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.hibernate.friend;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.friend.Friendship;
import me.minotopia.expvp.model.hibernate.BaseEntity;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence21.*;

/**
 * A friendship that is accessed using Hibernate.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
@Entity
@Table(name = "exp_friendship", schema = "mt_main")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HibernateFriendship extends BaseEntity implements Friendship {
    /*
   //////-------------------------------------------------\\\\\\
   ||  |      if you dare change this w/o updating the     |  ||
   ||  |              metamodel, ill murder u              |  ||
   ||\\\\-------------------------------------------------////||
   ||                                                         ||
   ||                               \O                        ||
 __||__                              |\                     __||__
|______|____________________________/_\____________________|______|
    ( from http://www.ascii-art.de/ascii/s/stickman.txt )
     */

    @Id
    @Column(name = "source_id")
    @OneToOne(targetEntity = HibernatePlayerData.class)
    private HibernatePlayerData source;

    @Column(name = "target_id")
    @OneToOne(targetEntity = HibernatePlayerData.class)
    private HibernatePlayerData target;

    public HibernateFriendship() {
        //default constructor required for Hibernate
    }

    public HibernateFriendship(HibernatePlayerData source, HibernatePlayerData target) {
        this.source = Preconditions.checkNotNull(source, "source");
        this.target = Preconditions.checkNotNull(target, "target");
    }

    @Override
    public HibernatePlayerData getSource() {
        return source;
    }

    @Override
    public HibernatePlayerData getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HibernateFriendship)) return false;
        HibernateFriendship that = (HibernateFriendship) o;
        return source.equals(that.source);
    }

    @Override
    public int hashCode() {
        return source.hashCode();
    }
}
