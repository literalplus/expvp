/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.assist;

import java.util.UUID;

/**
 * Keeps track of short-term hits on a player for detection of kill assistance.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-01
 */
public interface KillAssistService {
    Hits getHitsOn(UUID playerId);

    void clearHitsOn(UUID playerId);

    void expireOldData();
}
