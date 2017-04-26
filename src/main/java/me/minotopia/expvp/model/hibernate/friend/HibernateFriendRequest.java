/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.hibernate.friend;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.friend.FriendRequest;
import me.minotopia.expvp.model.hibernate.BaseEntity;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence21.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A friend request that is accessed using Hibernate.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
@Entity
@Table(name = "exp_friend_request", schema = "mt_main")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HibernateFriendRequest extends BaseEntity implements FriendRequest {
    @Id
    @Column(name = "uuid")
    private UUID uniqueId;

    @JoinColumn(name = "source_id")
    @OneToOne(targetEntity = HibernatePlayerData.class)
    private HibernatePlayerData source;

    @JoinColumn(name = "target_id")
    @OneToOne(targetEntity = HibernatePlayerData.class)
    private HibernatePlayerData target;

    public HibernateFriendRequest() {
        //default constructor required for Hibernate
    }

    public HibernateFriendRequest(HibernatePlayerData source, HibernatePlayerData target) {
        this.uniqueId = UUID.randomUUID();
        this.source = Preconditions.checkNotNull(source, "source");
        this.target = Preconditions.checkNotNull(target, "target");
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
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
    public boolean isValid() {
        return getCreationDate().atZone(ZoneId.systemDefault()).plusMinutes(5).isAfter(ZonedDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HibernateFriendRequest)) return false;

        HibernateFriendRequest that = (HibernateFriendRequest) o;

        return uniqueId.equals(that.uniqueId);
    }

    @Override
    public int hashCode() {
        return uniqueId.hashCode();
    }
}
