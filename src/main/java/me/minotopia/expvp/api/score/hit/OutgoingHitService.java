/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.hit;

import java.util.UUID;

/**
 * Keeps track of short-term hits by a player for score bonuses.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public interface OutgoingHitService {
    Hits getHitsBy(UUID playerId);

    void clearHitsBy(UUID playerId);

    void recordHitOnBy(UUID victimId, UUID culpritId, double damage);

    void expireOldData();
}
