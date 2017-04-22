/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.spawn;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Manages automatic spawn changes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-14
 */
public interface SpawnChangeService {
    boolean needsSpawnChange();

    LocalDateTime findNextSpawnChangeTime();

    LocalDateTime findLastSpawnChangeTime();

    Duration findTimeUntilNextChange();

    /**
     * @return the fractional progress between the last and next spawn change, meaning a result of 1.0 if the spawn
     * change would occur now and 0.0 if the spawn was just changed, but never less than zero and never more than 1.0
     */
    float findFractionProgressToNextSpawn();

    void registerSpawnChange();
}
