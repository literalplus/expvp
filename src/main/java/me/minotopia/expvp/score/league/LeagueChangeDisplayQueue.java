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

package me.minotopia.expvp.score.league;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.misc.PlayerInitService;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Queues league changes for display.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-20
 */
@Singleton
public class LeagueChangeDisplayQueue {
    private final Set<UUID> pendingLeagueChanges = new HashSet<>();

    @Inject
    public LeagueChangeDisplayQueue(PlayerInitService initService) {
        initService.registerDeInitHandler(this::purgePlayerLeagueChangeCache);
    }

    public void queueLeagueChange(UUID playerId) {
        pendingLeagueChanges.add(playerId);
    }

    public boolean unqueueLeagueChange(UUID playerId) {
        return pendingLeagueChanges.remove(playerId);
    }

    private void purgePlayerLeagueChangeCache(Player player) {
        pendingLeagueChanges.remove(player.getUniqueId());
    }
}
