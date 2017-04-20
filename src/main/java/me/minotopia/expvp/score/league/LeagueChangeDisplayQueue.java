/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.league;

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
