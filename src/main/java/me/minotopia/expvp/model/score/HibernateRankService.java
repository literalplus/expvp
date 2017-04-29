/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
