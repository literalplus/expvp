/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.player;

import me.minotopia.expvp.model.player.ObtainedSkill;
import me.minotopia.expvp.model.player.PlayerData;
import me.minotopia.expvp.skill.meta.Skill;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Manages PlayerData instances.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-14
 */
public class PlayerDataManager {
    private final SessionFactory sessionFactory;

    public PlayerDataManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public PlayerData findOrCreateDataFor(UUID playerId) {
        try(Session session = sessionFactory.openSession()) {
            PlayerData playerData = session.get(PlayerData.class, playerId);
            if(playerData == null) {
                playerData = new PlayerData(playerId);
                session.saveOrUpdate(playerData);
            }
            return playerData;
        }
    }

    public void saveData(PlayerData toSave) {
        try(Session session = sessionFactory.openSession()) {
            session.saveOrUpdate(toSave);
        }
    }

    public boolean hasObtainedSkill(UUID playerId, Skill skill) {
        PlayerData playerData = findOrCreateDataFor(playerId);
        return playerData.getSkills().stream()
                .anyMatch(skill::matches);
    }

    public Collection<String> getObtainedSkills(UUID playerId) {
        PlayerData playerData = findOrCreateDataFor(playerId);
        return playerData.getSkills().stream()
                .map(ObtainedSkill::getSkillId)
                .collect(Collectors.toList());
    }
}
