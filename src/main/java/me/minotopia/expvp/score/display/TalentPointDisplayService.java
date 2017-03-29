/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.display;

import com.google.inject.Inject;
import io.puharesource.mc.titlemanager.api.ActionbarTitleObject;
import me.minotopia.expvp.api.score.TalentPointService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

/**
 * Displays the current amount of talent points, and the amount of kills needed for the next talent point.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
public class TalentPointDisplayService {
    private final TalentPointService talentPoints;
    private final SessionProvider sessionProvider;

    @Inject
    public TalentPointDisplayService(TalentPointService talentPoints, SessionProvider sessionProvider) {
        this.talentPoints = talentPoints;
        this.sessionProvider = sessionProvider;
    }

    public void updateDisplayFor(Player player) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            int talentPoints = this.talentPoints.getCurrentTalentPointCount(player);
            int killsUntilNextTalentPoint = this.talentPoints.findKillsUntilNextTalentPoint(player);
            new ActionbarTitleObject(I18n.loc(
                    player, "score/tp.actionbar", talentPoints, killsUntilNextTalentPoint
            )).send(player);
            scoped.commitIfLastAndChanged();
        }
    }
}
