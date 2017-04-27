/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.score;

import com.google.inject.Inject;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.model.RankService;
import me.minotopia.expvp.util.SessionProvider;

import javax.persistence21.TypedQuery;

/**
 * Finds ranks with Hibernate.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-27
 */
public class HibernateRankService implements RankService {
    private final SessionProvider sessionProvider;

    @Inject
    public HibernateRankService(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Override
    public int findExpRank(PlayerData data) {
        return sessionProvider.inSessionAnd(scoped -> {
            TypedQuery<Integer> query = scoped.session().createQuery("SELECT COUNT(*) FROM HibernatePlayerData p " +
                    "WHERE p.exp > :myexp", Integer.class);
            query.setParameter("myexp", data.getExp());
            return query.getSingleResult();
        });
    }
}
