/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

import io.puharesource.mc.titlemanager.api.ActionbarTitleObject;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.TalentPointService;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.entity.Player;

/**
 * Displays the current amount of talent points, and the amount of kills needed for the next talent point.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
class TalentPointDisplayService {
    private final TalentPointService talentPoints;

    public TalentPointDisplayService(TalentPointService talentPoints) {
        this.talentPoints = talentPoints;
    }

    public void updateDisplay(Player player, PlayerData playerData) {
        int killsUntilNextTalentPoint = this.talentPoints.findKillsUntilNextTalentPoint(player);
        new ActionbarTitleObject(I18n.loc(
                player, "score/tp.actionbar", playerData.getTalentPoints(), killsUntilNextTalentPoint
        )).send(player);
    }
}
