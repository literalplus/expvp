/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.obtainment;

import com.google.inject.Inject;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.TalentPointService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

/**
 * A wrapper for skill obtainment services that adds cost checking and payments using {@link
 * PlayerData#getTalentPoints() books}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-11
 */
public class TalentPointResearchService extends SimpleResearchService {
    private final SessionProvider sessionProvider;
    private final TalentPointService talentPoints;

    @Inject
    public TalentPointResearchService(PlayerDataService playerDataService, SessionProvider sessionProvider,
                                      DisplayNameService displayNameService, TalentPointService talentPoints) {
        super(playerDataService, displayNameService);
        this.sessionProvider = sessionProvider;
        this.talentPoints = talentPoints;
    }

    @Override
    public void research(Player player, SimpleSkillTreeNode node) {
        sessionProvider.inSession(ignored -> {
            super.research(player, node);
            talentPoints.consumeTalentPoints(player, node.getValue().getTalentPointCost());
        });
    }
}
