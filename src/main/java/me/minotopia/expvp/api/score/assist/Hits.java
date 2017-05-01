/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.assist;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

/**
 * Stores data about recent hits on a specific player. Expires hits after a short, implementation-specific time.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-01
 */
public interface Hits {
    UUID getPlayerId();

    /**
     * @return the most recent hit on this instance's player, but only in a very short expiry timestamp that need not be
     * the same as the {@link #getExpiryDuration()}, otherwise an empty optional
     */
    Optional<Hit> getMostRecentHitIfNotExpired();

    /**
     * @param culpritId the culprit whose hit list to retrieve
     * @return the hit list with only non-expired hits for given culprit
     */
    HitList getHitList(UUID culpritId);

    void recordHitBy(UUID culpritId, double damage);

    /**
     * Expires hits and hit lists that are older than this instance's expiry time.
     */
    void expireOldHits();

    /**
     * @return the duration after which hits are expired
     */
    Duration getExpiryDuration();
}
