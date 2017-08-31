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
