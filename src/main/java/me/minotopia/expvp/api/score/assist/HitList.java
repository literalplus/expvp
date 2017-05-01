/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.assist;

import java.time.Duration;
import java.util.Collection;
import java.util.UUID;

/**
 * Stores and provides convenient access to the list of recent hits a culprit dealt on a known victim.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-01
 */
public interface HitList {
    UUID getCulpritId();

    Collection<? extends Hit> getRawHits();

    void expireHitsOlderThan(Duration expiryDuration);

    double getRecentDamageSum();
}
