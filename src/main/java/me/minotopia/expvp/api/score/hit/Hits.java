/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.hit;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Stores data about recent hits related to a specific player. Expires hits after a short, implementation-specific time.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-01
 */
public interface Hits {
    UUID getPlayerId();

    /**
     * @return the most recent hit related to this instance's player, but only in a very short expiry timestamp that
     * need not be the same as the {@link #getExpiryDuration()}, otherwise an empty optional
     */
    Optional<Hit> getMostRecentHitIfNotExpired();

    /**
     * @param peerId the other player whose hits to retrieve from this player's related hits
     * @return the hit list with only non-expired hits for given peer
     */
    HitList getHitList(UUID peerId);

    void recordHitInvolving(UUID peerId, double damage);

    /**
     * Expires hits and hit lists that are older than this instance's expiry time.
     */
    void expireOldHits();

    Stream<? extends Hit> allHits();

    /**
     * @return the duration after which hits are expired
     */
    Duration getExpiryDuration();
}
