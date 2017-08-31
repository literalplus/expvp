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

package me.minotopia.expvp.skill.obtainment;

import com.google.inject.Inject;
import me.minotopia.expvp.api.handler.HandlerService;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.points.TalentPointService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

/**
 * A wrapper for skill obtainment services that adds cost checking and payments using {@link
 * PlayerData#getAvailableTalentPoints() books}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-11
 */
public class TalentPointResearchService extends SimpleResearchService {
    private final SessionProvider sessionProvider;
    private final TalentPointService talentPoints;

    @Inject
    public TalentPointResearchService(PlayerDataService playerDataService, SessionProvider sessionProvider,
                                      DisplayNameService displayNameService, TalentPointService talentPoints,
                                      HandlerService handlerService) {
        super(playerDataService, displayNameService, handlerService);
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
