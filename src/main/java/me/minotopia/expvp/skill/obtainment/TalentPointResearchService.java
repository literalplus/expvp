/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.obtainment;

import com.google.inject.Inject;
import li.l1t.common.exception.UserException;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SkillTree;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;

import java.util.UUID;

/**
 * A wrapper for skill obtainment services that adds cost checking and payments using {@link
 * PlayerData#getTalentPoints() books}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-11
 */
public class TalentPointResearchService extends SimpleResearchService {
    private final PlayerDataService playerDataService;
    private final SessionProvider sessionProvider;

    @Inject
    public TalentPointResearchService(PlayerDataService playerDataService,
                                      SessionProvider sessionProvider, DisplayNameService displayNameService) {
        super(playerDataService, displayNameService);
        this.playerDataService = playerDataService;
        this.sessionProvider = sessionProvider;
    }

    @Override
    public void research(UUID playerId, Skill skill, SkillTree tree) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            scoped.tx();
            MutablePlayerData playerData = playerDataService.findOrCreateDataMutable(playerId);
            if (playerData.getTalentPoints() < skill.getBookCost()) {
                throw new UserException("Du hast dafür nicht genügend Talentpunkte!");
            }
            playerData.setTalentPoints(playerData.getTalentPoints() - skill.getBookCost()); //TODO: TalentPointService #740
            super.research(playerId, skill, tree);
            playerDataService.saveData(playerData);
            scoped.commitIfLast();
        } catch (Exception e) {
            throw sessionProvider.handleException(e);
        }
    }
}
