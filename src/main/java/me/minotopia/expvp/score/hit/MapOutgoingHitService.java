/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.hit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.api.score.hit.Hits;
import me.minotopia.expvp.api.score.hit.OutgoingHitService;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Keeps track of outgoing hits using a map.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
@Singleton
public class MapOutgoingHitService implements OutgoingHitService {
    private final Map<UUID, MapHits> hitsMap = new HashMap<>();
    private final Duration expiryDuration = Duration.ofMinutes(5);

    @Inject
    public MapOutgoingHitService(TaskService tasks, PlayerInitService initService) {
        tasks.repeating(this::expireOldData, Duration.ofMinutes(10));
        initService.registerDeInitHandler(player -> clearHitsBy(player.getUniqueId()));
    }

    @Override
    public Hits getHitsBy(UUID playerId) {
        return hitsMap.computeIfAbsent(playerId, this::createHitsFor);
    }

    private MapHits createHitsFor(UUID uuid) {
        return new MapHits(expiryDuration, expiryDuration, uuid);
    }

    @Override
    public void recordHitOnBy(UUID victimId, UUID culpritId, double damage) {
        getHitsBy(victimId).recordHitInvolving(culpritId, damage);
    }

    @Override
    public void clearHitsBy(UUID playerId) {
        hitsMap.remove(playerId);
    }

    @Override
    public void expireOldData() {
        hitsMap.values().forEach(MapHits::expireOldHits);
    }
}
