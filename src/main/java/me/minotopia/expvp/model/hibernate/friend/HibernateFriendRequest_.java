/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.hibernate.friend;

import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;

import javax.persistence21.metamodel.SingularAttribute;
import javax.persistence21.metamodel.StaticMetamodel;
import java.util.UUID;

/**
 * The metamodel for {@link HibernateFriendRequest}, manually updated because of issues with Bukkit shipping the wrong
 * JPA version and build tools being unable to detect shaded annotations.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
@StaticMetamodel(HibernateFriendRequest.class)
public class HibernateFriendRequest_ {
    public static volatile SingularAttribute<HibernateFriendRequest, UUID> uniqueId;
    public static volatile SingularAttribute<HibernateFriendRequest, HibernatePlayerData> source;
    public static volatile SingularAttribute<HibernateFriendRequest, HibernatePlayerData> target;
}
