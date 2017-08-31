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

package me.minotopia.expvp.model.hibernate.friend;

import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;

import javax.persistence21.metamodel.SingularAttribute;
import javax.persistence21.metamodel.StaticMetamodel;

/**
 * The metamodel for {@link HibernateFriendship}, manually updated because of issues with Bukkit shipping the wrong JPA
 * version and build tools being unable to detect shaded annotations.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
@StaticMetamodel(HibernateFriendship.class)
public class HibernateFriendship_ {
    public static volatile SingularAttribute<HibernateFriendship, HibernatePlayerData> source;
    public static volatile SingularAttribute<HibernateFriendship, HibernatePlayerData> target;
}
