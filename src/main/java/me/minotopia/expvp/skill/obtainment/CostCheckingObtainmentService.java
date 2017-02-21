/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.obtainment;

import li.l1t.common.exception.UserException;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.api.service.SkillObtainmentService;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.hibernate.HibernateException;

import java.util.Collection;
import java.util.UUID;

/**
 * A wrapper for skill obtainment services that adds cost checking and payments using {@link
 * PlayerData#getBooks() books}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-11
 */
public class CostCheckingObtainmentService implements SkillObtainmentService {
    private final SkillObtainmentService proxy;
    private final PlayerDataService playerDataService;
    private final SessionProvider sessionProvider;

    public CostCheckingObtainmentService(SkillObtainmentService proxy,
                                         PlayerDataService playerDataService,
                                         SessionProvider sessionProvider) {
        this.proxy = proxy;
        this.playerDataService = playerDataService;
        this.sessionProvider = sessionProvider;
    }

    @Override
    public void addObtainedSkill(UUID playerId, Skill skill) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            scoped.tx();
            MutablePlayerData playerData = playerDataService.findOrCreateDataMutable(playerId);
            if (playerData.getBooks() < skill.getBookCost()) {
                throw new UserException("Du hast dafür nicht genügend Skillpunkte!");
            }
            playerData.setBooks(playerData.getBooks() - skill.getBookCost());
            proxy.addObtainedSkill(playerId, skill);
            playerDataService.saveData(playerData);
            scoped.commitIfLast();
        } catch (HibernateException e) {
            throw sessionProvider.handleException(e);
        }
    }

    @Override
    public void removeObtainedSkill(UUID playerId, Skill skill) {
        proxy.removeObtainedSkill(playerId, skill);
    }

    @Override
    public boolean hasObtainedSkill(UUID playerId, Skill skill) {
        return proxy.hasObtainedSkill(playerId, skill);
    }

    @Override
    public Collection<String> getObtainedSkills(UUID playerId) {
        return proxy.getObtainedSkills(playerId);
    }
}
