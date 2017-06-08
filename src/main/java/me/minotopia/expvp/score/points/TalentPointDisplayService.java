/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.points;

import io.puharesource.mc.titlemanager.api.ActionbarTitleObject;
import me.minotopia.expvp.api.score.points.TalentPointObjective;
import me.minotopia.expvp.api.score.points.TalentPointService;
import me.minotopia.expvp.api.score.points.TalentPointType;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.Plurals;
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

    public void displayCurrentTP(Player player) {
        sendCombatTPStatusMessage(player);
    }

    private void sendCombatTPStatusMessage(Player player) {
        int availablePoints = talentPoints.getCurrentTalentPointCount(player);
        TalentPointObjective objective = this.talentPoints.nextPointObjective(player, TalentPointType.COMBAT);
        sendActionbar(
                player, "score!tp.actionbar",
                Plurals.talentPointPlural(availablePoints),
                objective.getDescription()
        );
    }

    private void sendActionbar(Player receiver, String messageKey, Object... args) {
        new ActionbarTitleObject(I18n.loc(receiver, messageKey, args)).send(receiver);
    }

    public void displayTPGained(Player player, int count, TalentPointType type) {
        if (count <= 0) {
            displayCurrentTP(player);
        } else {
            sendActionbar(
                    player, "score!tp.actionbar.gain",
                    Plurals.talentPointPlural(count), type.getDescription()
            );
        }
    }

    public void displayTPSpent(Player player, int count) {
        if (count <= 0) {
            displayCurrentTP(player);
        } else {
            sendActionbar(
                    player, "score!tp.actionbar.lose",
                    Plurals.talentPointPlural(count)
            );
        }
    }
}
