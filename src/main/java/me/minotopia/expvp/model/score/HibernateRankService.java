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

package me.minotopia.expvp.model.score;

import com.google.inject.Inject;
import li.l1t.common.collections.cache.GuavaMapCache;
import li.l1t.common.collections.cache.MapCache;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.model.RankService;
import me.minotopia.expvp.util.SessionProvider;

import javax.persistence21.TypedQuery;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Finds ranks with Hibernate.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-27
 */
public class HibernateRankService implements RankService {
    private final SessionProvider sessionProvider;
    private final MapCache<UUID, Long> expRankCache = new GuavaMapCache<>(30, TimeUnit.SECONDS);

    @Inject
    public HibernateRankService(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Override
    public long findExpRank(PlayerData data) {
        return sessionProvider.inSessionAnd(scoped -> {
            TypedQuery<Long> query = scoped.session().createQuery("SELECT COUNT(*) FROM HibernatePlayerData p " +
                    "WHERE p.exp > :myexp", Long.class);
            query.setParameter("myexp", data.getExp());
            return query.getSingleResult() + 1;
        });
    }

    @Override
    public long getExpRank(PlayerData data) {
        return expRankCache.getOrCompute(data.getUniqueId(), ignored -> findExpRank(data));
    }
}
