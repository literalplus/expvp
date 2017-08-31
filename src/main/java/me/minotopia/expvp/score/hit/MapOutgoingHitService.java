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
