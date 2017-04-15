/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.spawn;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Casts and keeps track of players' votes on what spawn should be selected next.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-15
 */
public interface SpawnVoteService {
    void castVoteFor(UUID playerId, MapSpawn spawn);

    Optional<MapSpawn> findVote(UUID playerId);

    void retractVote(UUID playerId);

    Map<UUID, MapSpawn> getCurrentVotes();

    /**
     * Finds the spawn that would currently win the election. <p>If there are multiple spawns with
     * the same amount of votes, a spawn is selected via an implementation-specific algorithm. The
     * algorithm need not be deterministic, that means that the resolution to such a conflict may be
     * just rolling a dice.</p>
     *
     * @return an optional containing the spawn that would currently win the election, or an empty
     * optional if there are not spawns
     */
    Optional<MapSpawn> findCurrentlyWinningSpawn();

    void resetAllVotes();
}
