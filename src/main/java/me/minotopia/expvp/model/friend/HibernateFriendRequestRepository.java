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

package me.minotopia.expvp.model.friend;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.exception.DatabaseException;
import me.minotopia.expvp.api.friend.FriendRequest;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.model.friend.FriendRequestRepository;
import me.minotopia.expvp.model.hibernate.friend.HibernateFriendRequest;
import me.minotopia.expvp.model.hibernate.friend.HibernateFriendRequest_;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import me.minotopia.expvp.model.player.HibernatePlayerDataService;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence21.criteria.CriteriaBuilder;
import javax.persistence21.criteria.CriteriaQuery;
import javax.persistence21.criteria.Predicate;
import javax.persistence21.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides access to friend requests via the Hibernate API.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
@Singleton
public class HibernateFriendRequestRepository implements FriendRequestRepository {
    private final SessionProvider sessionProvider;
    private final HibernatePlayerDataService players;

    @Inject
    public HibernateFriendRequestRepository(SessionProvider sessionProvider, HibernatePlayerDataService players) {
        this.sessionProvider = sessionProvider;
        this.players = players;
    }

    @Override
    public Collection<FriendRequest> findReceivedRequests(PlayerData data) {
        return sessionProvider.inSessionAnd(scoped -> {
            scoped.tx();
            return filterExpired(createTargetQuery(data, scoped).getResultList());
        });
    }

    private Query<FriendRequest> createTargetQuery(PlayerData data, ScopedSession scoped) {
        CriteriaQuery<FriendRequest> criteria = createTargetCriteria(data, scoped.session());
        return scoped.session().createQuery(criteria);
    }

    private CriteriaQuery<FriendRequest> createTargetCriteria(PlayerData data, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FriendRequest> criteria = builder.createQuery(FriendRequest.class);
        Root<HibernateFriendRequest> root = criteria.from(HibernateFriendRequest.class);
        criteria.where(targetIsPredicate(data, builder, root));
        criteria.select(root);
        return criteria;
    }

    private Predicate targetIsPredicate(PlayerData data, CriteriaBuilder builder, Root<HibernateFriendRequest> root) {
        return builder.equal(root.get(HibernateFriendRequest_.target), data);
    }

    private List<FriendRequest> filterExpired(Collection<FriendRequest> input) {
        return input.stream()
                .filter(this::deleteIfInvalid)
                .collect(Collectors.toList());
    }

    private boolean deleteIfInvalid(FriendRequest req) {
        boolean valid = req.isValid();
        if (!valid) {
            delete(req);
        }
        return valid;
    }

    @Override
    public Optional<FriendRequest> findSentRequest(PlayerData data) {
        return sessionProvider.inSessionAnd(scoped -> {
            scoped.tx();
            List<FriendRequest> results = filterExpired(createSourceQuery(data, scoped).getResultList());
            if (results.isEmpty()) {
                return Optional.empty();
            } else if (results.size() == 1) {
                return Optional.of(results.get(0));
            } else {
                throw new DatabaseException(new IllegalStateException("More than one friend request from " + data.getUniqueId()));
            }
        });
    }

    private Query<FriendRequest> createSourceQuery(PlayerData data, ScopedSession scoped) {
        CriteriaQuery<FriendRequest> criteria = createSourceCriteria(data, scoped.session());
        return scoped.session().createQuery(criteria);
    }

    private CriteriaQuery<FriendRequest> createSourceCriteria(PlayerData data, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FriendRequest> criteria = builder.createQuery(FriendRequest.class);
        Root<HibernateFriendRequest> root = criteria.from(HibernateFriendRequest.class);
        criteria.where(sourceIsPredicate(data, builder, root));
        criteria.select(root);
        return criteria;
    }

    private Predicate sourceIsPredicate(PlayerData data, CriteriaBuilder builder, Root<HibernateFriendRequest> root) {
        return builder.equal(root.get(HibernateFriendRequest_.source), data);
    }

    @Override
    public FriendRequest create(PlayerData source, PlayerData target) {
        return sessionProvider.inSessionAnd(scoped -> {
            scoped.tx();
            HibernateFriendRequest request = new HibernateFriendRequest(
                    makeHibernate(source), makeHibernate(target)
            );
            scoped.session().save(request);
            return request;
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

    @Override
    public void delete(FriendRequest request) {
        Preconditions.checkNotNull(request, "request");
        Preconditions.checkArgument(request instanceof HibernateFriendRequest, "unexpected type: ", request);
        sessionProvider.inSession(scoped -> {
            scoped.tx();
            scoped.session().delete(request);
        });
    }
}
