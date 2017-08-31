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

package me.minotopia.expvp.api.spawn;

import org.bukkit.command.CommandSender;

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
    long findVoteCount(MapSpawn spawn);

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

    void showCurrentVotesTo(CommandSender sender, boolean showVoteButton);
}
