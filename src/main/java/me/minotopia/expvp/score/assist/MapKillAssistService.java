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

package me.minotopia.expvp.score.assist;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.api.score.assist.KillAssistService;
import me.minotopia.expvp.api.score.hit.Hits;
import me.minotopia.expvp.score.hit.MapHits;

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

    @Inject
    public MapKillAssistService(TaskService tasks, PlayerInitService initService) {
        tasks.repeating(this::expireOldData, Duration.ofMinutes(5));
        initService.registerDeInitHandler(player -> clearHitsOn(player.getUniqueId()));
    }

    @Override
    public Hits getHitsOn(UUID playerId) {
        return hitsMap.computeIfAbsent(playerId, this::createHitsFor);
    }

    private MapHits createHitsFor(UUID uuid) {
        return new MapHits(expiryDuration, mostRecentExpiryDuration, uuid);
    }

    @Override
    public void recordHitOnBy(UUID victimId, UUID culpritId, double damage) {
        getHitsOn(victimId).recordHitInvolving(culpritId, damage);
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
