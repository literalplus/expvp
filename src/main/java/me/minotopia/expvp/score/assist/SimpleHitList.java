/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.assist;

import me.minotopia.expvp.api.score.assist.Hit;
import me.minotopia.expvp.api.score.assist.HitList;

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
