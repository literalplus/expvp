/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.points.strategy;

import com.google.inject.Inject;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.hit.Hit;
import me.minotopia.expvp.api.score.hit.OutgoingHitService;
import me.minotopia.expvp.api.score.points.TalentPointObjective;
import me.minotopia.expvp.api.score.points.TalentPointType;
import me.minotopia.expvp.api.score.points.TalentPointTypeStrategy;
import me.minotopia.expvp.score.points.objective.SimpleObjective;

/**
 * Grants Talent Points if a specific amount of damage was dealt to other players recently.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public class RecentDamageTalentPointStrategy implements TalentPointTypeStrategy {
    private static final double POINT_DAMAGE_THRESHOLD = 20D;
    private final TalentPointType TYPE = TalentPointType.RECENT_DAMAGE;
    private final OutgoingHitService hitService;

    @Inject
    public RecentDamageTalentPointStrategy(OutgoingHitService hitService) {
        this.hitService = hitService;
    }

    @Override
    public TalentPointType getType() {
        return TYPE;
    }

    @Override
    public int findDeservedPoints(MutablePlayerData playerData) {
        if (hitService.getHitsBy(playerData.getUniqueId()).allHits()
                .mapToDouble(Hit::getDamage)
                .sum() >= POINT_DAMAGE_THRESHOLD) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getPointsUntilLimit(PlayerData playerData) {
        return Math.max(0, TYPE.getLimit() - playerData.getTalentPointCount(TYPE));
    }

    @Override
    public TalentPointObjective calculateObjectiveForNext(MutablePlayerData playerData) {
        return new SimpleObjective(Message.of("tp.obj.recent_damage"), TYPE);
    }
}
