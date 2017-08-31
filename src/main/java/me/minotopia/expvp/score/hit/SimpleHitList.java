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
import me.minotopia.expvp.api.score.hit.HitList;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * A simple implementation of a hit list.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-01
 */
public class SimpleHitList implements HitList {
    private final UUID peerId;
    private final List<SimpleHit> hits = new ArrayList<>(32);

    public SimpleHitList(UUID peerId) {
        this.peerId = peerId;
    }

    @Override
    public UUID getPeerId() {
        return peerId;
    }

    @Override
    public Collection<SimpleHit> getRawHits() {
        return hits;
    }

    public Hit recordHit(double damage) {
        SimpleHit hit = new SimpleHit(peerId, damage);
        hits.add(hit);
        return hit;
    }

    public boolean isEmpty() {
        return hits.isEmpty();
    }

    @Override
    public void expireHitsOlderThan(Duration expiryDuration) {
        hits.removeIf(hit -> hit.isOlderThan(expiryDuration));
    }

    @Override
    public double getRecentDamageSum() {
        return hits.stream().mapToDouble(Hit::getDamage).sum();
    }
}
