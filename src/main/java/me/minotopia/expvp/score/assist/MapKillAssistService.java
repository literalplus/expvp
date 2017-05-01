/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.assist;

import com.google.inject.Singleton;
import me.minotopia.expvp.api.score.assist.Hits;
import me.minotopia.expvp.api.score.assist.KillAssistService;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple map-based implementation of a kill assist service.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-01
 */
@Singleton
public class MapKillAssistService implements KillAssistService {
    private final Map<UUID, MapHits> hitsMap = new HashMap<>();
    private final Duration expiryDuration = Duration.ofMinutes(3);
    private final Duration mostRecentExpiryDuration = Duration.ofSeconds(20);

    @Override
    public Hits getHitsOn(UUID playerId) {
        return hitsMap.computeIfAbsent(playerId, this::createHitsFor);
    }

    private MapHits createHitsFor(UUID uuid) {
        return new MapHits(expiryDuration, mostRecentExpiryDuration, uuid);
    }

    @Override
    public void clearHitsOn(UUID playerId) {
        hitsMap.remove(playerId);
    }

    @Override
    public void expireOldData() {
        hitsMap.values().forEach(MapHits::expireOldHits);
    }
}
