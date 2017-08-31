/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.minotopia.expvp.score.hit;

import me.minotopia.expvp.api.score.hit.Hit;
import me.minotopia.expvp.api.score.hit.Hits;

import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

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
        if (mostRecentHit != null && mostRecentHit.isOlderThan(mostRecentExpiryDuration)) {
            mostRecentHit = null;
        }
        return Optional.ofNullable(mostRecentHit);
    }

    @Override
    public SimpleHitList getHitList(UUID peerId) {
        SimpleHitList hitList = hitLists.computeIfAbsent(peerId, SimpleHitList::new);
        hitList.expireHitsOlderThan(getExpiryDuration());
        return hitList;
    }

    @Override
    public void recordHitInvolving(UUID peerId, double damage) {
        mostRecentHit = getHitList(peerId).recordHit(damage);
    }

    @Override
    public void expireOldHits() {
        hitLists.values().forEach(hitList -> hitList.expireHitsOlderThan(getExpiryDuration()));
        hitLists.values().removeIf(SimpleHitList::isEmpty);
    }

    @Override
    public Stream<SimpleHit> allHits() {
        expireOldHits();
        return hitLists.values().stream()
                .map(SimpleHitList::getRawHits)
                .flatMap(Collection::stream);
    }

    @Override
    public Duration getExpiryDuration() {
        return expiryDuration;
    }
}
