/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.spawn;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.spawn.*;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.Plurals;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * Task that checks whether a spawn change is necessary, and, if so, changes the spawn. Also
 * notifies all online players of the current status
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-14
 */
@Singleton
@ConstructOnEnable
public class SpawnVoteTask implements Runnable {
    private final SpawnDisplayService displayService;
    private final SpawnService spawns;
    private final SpawnChangeService changeService;
    private final SpawnVoteService voteService;
    private final Server server;
    private long lastReminderMinute;

    @Inject
    public SpawnVoteTask(SpawnDisplayService displayService, SpawnService spawns,
                         SpawnChangeService changeService, SpawnVoteService voteService,
                         TaskService tasks, Server server) {
        this.displayService = displayService;
        this.spawns = spawns;
        this.changeService = changeService;
        this.voteService = voteService;
        this.server = server;
        tasks.repeating(this, Duration.ofSeconds(30));
    }

    @Override
    public void run() {
        if (changeService.needsSpawnChange()) {
            voteService.findCurrentlyWinningSpawn().ifPresent(this::forceNextSpawn);
        } else {
            long minutesUntilChange = changeService.findTimeUntilNextChange().toMinutes();
            if (minutesUntilChange > 0 && (minutesUntilChange == 5 || (minutesUntilChange % 15) == 0)) {
                remindEligiblePlayersToVote(minutesUntilChange);
            }
        }
        displayService.updateForAllPlayers();
    }

    private void remindEligiblePlayersToVote(long minutesUntilChange) {
        if (aReminderWasAlreadySentForThisMinute(minutesUntilChange)) {
            return;
        }
        lastReminderMinute = minutesUntilChange;
        server.getOnlinePlayers().stream()
                .filter(this::hasNotCastAVote)
                .forEach(remindToCastAVote(minutesUntilChange));
    }

    private boolean aReminderWasAlreadySentForThisMinute(long minutesUntilChange) {
        return lastReminderMinute == minutesUntilChange;
    }

    private Consumer<Player> remindToCastAVote(long minutesUntilChange) {
        return player -> I18n.sendLoc(
                player, Format.broadcast("spawn!vote.chat-reminder", Plurals.minutePlural(minutesUntilChange))
        );
    }

    private void forceNextSpawn(MapSpawn spawn) {
        spawns.forceNextSpawn(spawn);
        voteService.resetAllVotes();
        changeService.registerSpawnChange();
        server.getOnlinePlayers()
                .forEach(spawns::teleportToSpawnIfPossible);
        displayService.updateForAllPlayers();
    }

    private boolean hasNotCastAVote(Player player) {
        return !voteService.findVote(player.getUniqueId()).isPresent();
    }
}
