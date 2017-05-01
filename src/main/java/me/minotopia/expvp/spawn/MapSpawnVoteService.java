/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.spawn;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.api.spawn.SpawnVoteService;
import org.apache.commons.lang.math.RandomUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Keeps track of players' spawn votes using a map.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-15
 */
@Singleton
public class MapSpawnVoteService implements SpawnVoteService {
    private final Map<UUID, MapSpawn> currentVotes = new HashMap<>();
    private final SpawnService spawns;

    @Inject
    public MapSpawnVoteService(SpawnService spawns) {
        this.spawns = spawns;
    }

    @Override
    public Optional<MapSpawn> findCurrentlyWinningSpawn() {
        Map<MapSpawn, Long> spawnToVotes = currentVotes.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue, Collectors.counting()
                ));
        long maxVotes = spawnToVotes.values().stream().max(Comparator.naturalOrder()).orElse(0L);
        List<MapSpawn> bestMaps = spawnToVotes.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVotes)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (bestMaps.size() == 1) {
            return bestMaps.stream().findFirst();
        } else if (!bestMaps.isEmpty()) {
            return Optional.of(randomSpawnFrom(bestMaps));
        }
        List<MapSpawn> allSpawns = this.spawns.getSpawns();
        if (allSpawns.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(randomSpawnFrom(allSpawns));
    }

    @Override
    public long findVoteCount(MapSpawn spawn) {
        return currentVotes.values().stream()
                .filter(spawn::equals)
                .count();
    }

    private MapSpawn randomSpawnFrom(List<MapSpawn> input) {
        int choice = RandomUtils.nextInt(input.size());
        return input.get(choice);
    }

    @Override
    public void castVoteFor(UUID playerId, MapSpawn spawn) {
        Preconditions.checkNotNull(spawn, "spawn");
        Preconditions.checkNotNull(playerId, "playerId");
        currentVotes.put(playerId, spawn);
    }

    @Override
    public Optional<MapSpawn> findVote(UUID playerId) {
        return Optional.ofNullable(currentVotes.get(playerId));
    }

    @Override
    public void retractVote(UUID playerId) {
        Preconditions.checkNotNull(playerId, "playerId");
        currentVotes.remove(playerId);
    }

    @Override
    public void resetAllVotes() {
        currentVotes.clear();
    }

    @Override
    public Map<UUID, MapSpawn> getCurrentVotes() {
        return ImmutableMap.copyOf(currentVotes);
    }
}
