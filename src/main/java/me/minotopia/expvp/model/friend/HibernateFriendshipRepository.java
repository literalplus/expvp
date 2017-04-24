/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.friend;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.exception.DatabaseException;
import me.minotopia.expvp.api.friend.Friendship;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.model.friend.FriendshipRepository;
import me.minotopia.expvp.model.hibernate.friend.HibernateFriendship;
import me.minotopia.expvp.model.hibernate.friend.HibernateFriendship_;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import me.minotopia.expvp.model.player.HibernatePlayerDataService;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.hibernate.query.Query;

import javax.persistence21.criteria.CriteriaBuilder;
import javax.persistence21.criteria.CriteriaQuery;
import javax.persistence21.criteria.Predicate;
import javax.persistence21.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Manages HibernateFriendship instances using a Hibernate backend.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
@Singleton
public class HibernateFriendshipRepository implements FriendshipRepository {
    private final SessionProvider sessionProvider;
    private final HibernatePlayerDataService players;

    @Inject
    public HibernateFriendshipRepository(SessionProvider sessionProvider, HibernatePlayerDataService players) {
        this.sessionProvider = sessionProvider;
        this.players = players;
    }

    @Override
    public Optional<Friendship> findFriendshipWith(PlayerData playerData) {
        return sessionProvider.inSessionAnd(scoped -> {
            scoped.tx();
                    List<HibernateFriendship> results = createFriendshipWithQuery(playerData, scoped).getResultList();
                    if (results.isEmpty()) {
                        return Optional.empty();
                    } else if (results.size() == 1) {
                        return Optional.of(results.get(0));
                    } else {
                        throw new DatabaseException(new IllegalStateException(
                                "More than one friendship for player " + playerData.getUniqueId()
                        ));
                    }
                }
        );
    }

    private Query<HibernateFriendship> createFriendshipWithQuery(PlayerData playerData, ScopedSession scoped) {
        CriteriaQuery<HibernateFriendship> criteria = createFriendshipWithCriteria(
                playerData, scoped.session().getCriteriaBuilder()
        );
        return scoped.session().createQuery(criteria);
    }

    private CriteriaQuery<HibernateFriendship> createFriendshipWithCriteria(PlayerData playerData, CriteriaBuilder builder) {
        CriteriaQuery<HibernateFriendship> criteria = builder.createQuery(HibernateFriendship.class);
        Root<HibernateFriendship> root = criteria.from(HibernateFriendship.class);
        criteria.where(friendshipWithPredicate(playerData, builder, root));
        return criteria;
    }

    private Predicate friendshipWithPredicate(PlayerData playerData, CriteriaBuilder builder, Root<HibernateFriendship> root) {
        return builder.or(
                builder.equal(root.get(HibernateFriendship_.source), playerData),
                builder.equal(root.get(HibernateFriendship_.target), playerData)
        );
    }

    @Override
    public void delete(Friendship friendship) {
        Preconditions.checkArgument(friendship instanceof HibernateFriendship,
                "expected HibernateFriendship, got: ", friendship, friendship.getClass());
        sessionProvider.inSession(scoped -> {
            scoped.tx();
            scoped.session().delete(friendship);
        });
    }

    @Override
    public HibernateFriendship create(PlayerData source, PlayerData target) {
        return sessionProvider.inSessionAnd(scoped -> {
            HibernateFriendship friendship = new HibernateFriendship(
                    makeHibernate(source),
                    makeHibernate(target)
            );
            scoped.session().save(friendship);
            return friendship;
        });
    }

    private HibernatePlayerData makeHibernate(PlayerData data) {
        Preconditions.checkNotNull(data, "data");
        if (data instanceof HibernatePlayerData) {
            return (HibernatePlayerData) data;
        } else {
            return players.findOrCreateData(data.getUniqueId());
        }
    }
}
