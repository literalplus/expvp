/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.assist;

import me.minotopia.expvp.api.score.assist.Hit;

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
