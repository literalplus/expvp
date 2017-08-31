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

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * A simple implementation of a hit.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-01
 */
public class SimpleHit implements Hit {
    private final UUID culpritId;
    private final Instant instant;
    private final double damage;

    public SimpleHit(UUID culpritId, double damage) {
        this.culpritId = culpritId;
        this.instant = Instant.now();
        this.damage = damage;
    }

    @Override
    public UUID getCulpritId() {
        return culpritId;
    }

    @Override
    public Instant getInstant() {
        return instant;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public boolean isOlderThan(Duration duration) {
        return getInstant().plus(duration).isBefore(Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleHit)) return false;
        SimpleHit simpleHit = (SimpleHit) o;
        return Double.compare(simpleHit.damage, damage) == 0 && instant.equals(simpleHit.instant);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = instant.hashCode();
        temp = Double.doubleToLongBits(damage);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "SimpleHit[" + damage + " at " + instant + "]";
    }
}
