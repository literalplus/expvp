/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.hibernate.player;

import javax.persistence21.metamodel.SingularAttribute;
import javax.persistence21.metamodel.StaticMetamodel;
import java.util.Locale;
import java.util.UUID;

/**
 * The metamodel for {@link HibernatePlayerData}, manually updated because of issues with Bukkit shipping the wrong JPA
 * version and build tools being unable to detect shaded annotations.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
@StaticMetamodel(HibernatePlayerData.class)
public class HibernatePlayerData_ {
    public static volatile SingularAttribute<HibernatePlayerData, UUID> uuid;
    public static volatile SingularAttribute<HibernatePlayerData, Integer> totalKills;
    public static volatile SingularAttribute<HibernatePlayerData, Integer> totalDeaths;
    public static volatile SingularAttribute<HibernatePlayerData, Integer> totalKillAssists;
    public static volatile SingularAttribute<HibernatePlayerData, Integer> currentKills;
    public static volatile SingularAttribute<HibernatePlayerData, Integer> currentDeaths;
    public static volatile SingularAttribute<HibernatePlayerData, String> leagueName;
    public static volatile SingularAttribute<HibernatePlayerData, Integer> exp;
    public static volatile SingularAttribute<HibernatePlayerData, Integer> talentPoints;
    public static volatile SingularAttribute<HibernatePlayerData, Locale> locale;
    public static volatile SingularAttribute<HibernatePlayerData, Boolean> customLocale;
    public static volatile SingularAttribute<HibernatePlayerData, Integer> bestKillStreak;
}
