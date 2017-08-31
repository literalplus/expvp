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
