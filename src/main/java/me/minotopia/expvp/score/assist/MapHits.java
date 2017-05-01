/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.assist;

import me.minotopia.expvp.api.score.assist.Hit;
import me.minotopia.expvp.api.score.assist.Hits;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Keeps hits on a single victim in a map.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-01
 */
public class MapHits implements Hits {
    private final Map<UUID, SimpleHitList> hitLists = new HashMap<>();
    private final Duration expiryDuration;
    private final Duration mostRecentExpiryDuration;
    private final UUID playerId;
    private Hit mostRecentHit;

    public MapHits(Duration expiryDuration, Duration mostRecentExpiryDuration, UUID playerId) {
        this.expiryDuration = expiryDuration;
        this.mostRecentExpiryDuration = mostRecentExpiryDuration;
        this.playerId = playerId;
    }

    @Override
    public UUID getPlayerId() {
        return playerId;
    }

    @Override
    public Optional<Hit> getMostRecentHitIfNotExpired() {
        if (mostRecentHit.isOlderThan(mostRecentExpiryDuration)) {
            mostRecentHit = null;
        }
        return Optional.ofNullable(mostRecentHit);
    }

    @Override
    public SimpleHitList getHitList(UUID culpritId) {
        SimpleHitList hitList = hitLists.computeIfAbsent(culpritId, SimpleHitList::new);
        hitList.expireHitsOlderThan(getExpiryDuration());
        return hitList;
    }

    @Override
    public void recordHitBy(UUID culpritId, double damage) {
        mostRecentHit = getHitList(culpritId).recordHit(damage);
    }

    @Override
    public void expireOldHits() {
        hitLists.values().forEach(hitList -> hitList.expireHitsOlderThan(getExpiryDuration()));
        hitLists.values().removeIf(SimpleHitList::isEmpty);
    }

    @Override
    public Duration getExpiryDuration() {
        return expiryDuration;
    }
}
