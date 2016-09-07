/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.player;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.model.hibernate.player.HibernatePlayerData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.UUID;

/**
 * Manages PlayerData instances using a Hibernate backend.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-14
 */
public class HibernatePlayerDataService implements PlayerDataService {
    private final SessionFactory sessionFactory;

    public HibernatePlayerDataService(SessionFactory sessionFactory) {
        this.sessionFactory = Preconditions.checkNotNull(sessionFactory, "sessionFactory");
    }

    @Override
    public PlayerData findOrCreateData(UUID playerId) {
        return findOrCreateDataMutable(playerId); //read-only is a type safety thing only, this simplifies usage
    }

    @Override
    public HibernatePlayerData findOrCreateDataMutable(UUID playerId) {
        Preconditions.checkNotNull(playerId, "playerId");
        try (Session session = sessionFactory.openSession()) {
            HibernatePlayerData playerData = session.get(HibernatePlayerData.class, playerId);
            if (playerData == null) {
                playerData = new HibernatePlayerData(playerId);
                session.saveOrUpdate(playerData);
            }
            return playerData;
        }
    }

    @Override
    public void saveData(MutablePlayerData toSave) {
        try (Session session = sessionFactory.openSession()) {
            session.saveOrUpdate(toSave);
        }
    }
}
