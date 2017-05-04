/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData_;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence21.criteria.CriteriaBuilder;
import javax.persistence21.criteria.CriteriaQuery;
import javax.persistence21.criteria.Root;
import javax.persistence21.metamodel.SingularAttribute;
import java.util.Collection;

/**
 * Retrieves top players based on a stat.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
@Singleton
public class HibernatePlayerTopRepository {
    private final SessionProvider sessionProvider;

    @Inject
    public HibernatePlayerTopRepository(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public Collection<PlayerData> findTopNByTotalKills(int limit) {
        return topN(limit, HibernatePlayerData_.totalKills);
    }

    public Collection<PlayerData> findTopNByExp(int limit) {
        return topN(limit, HibernatePlayerData_.exp);
    }

    public Collection<PlayerData> findTopNByBestStreak(int limit) {
        return topN(limit, HibernatePlayerData_.bestKillStreak);
    }

    private Collection<PlayerData> topN(int limit, SingularAttribute<HibernatePlayerData, Integer> attribute) {
        return sessionProvider.inSessionAnd(scoped -> {
            scoped.tx();
            return createTopNQuery(scoped, limit, attribute).getResultList();
        });
    }

    private Query<PlayerData> createTopNQuery(ScopedSession scoped, int limit, SingularAttribute<HibernatePlayerData, Integer> attribute) {
        CriteriaQuery<PlayerData> criteria = createTopNCriteria(scoped.session(), attribute);
        return scoped.session().createQuery(criteria)
                .setMaxResults(limit);
    }

    private CriteriaQuery<PlayerData> createTopNCriteria(Session session, SingularAttribute<HibernatePlayerData, Integer> attribute) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PlayerData> criteria = builder.createQuery(PlayerData.class);
        Root<HibernatePlayerData> root = criteria.from(HibernatePlayerData.class);
        criteria.orderBy(builder.desc(root.get(attribute)));
        criteria.select(root);
        return criteria;
    }
}
