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
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnChangeService;
import me.minotopia.expvp.api.spawn.SpawnDisplayService;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.api.spawn.SpawnVoteService;
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
public class SpawnVoteTask implements Runnable {
    private final SpawnDisplayService displayService;
    private final SpawnService spawns;
    private final SpawnChangeService changeService;
    private final SpawnVoteService voteService;
    private final TaskService tasks;
    private final Server server;

    @Inject
    public SpawnVoteTask(SpawnDisplayService displayService, SpawnService spawns,
                         SpawnChangeService changeService, SpawnVoteService voteService,
                         TaskService tasks, Server server) {
        this.displayService = displayService;
        this.spawns = spawns;
        this.changeService = changeService;
        this.voteService = voteService;
        this.tasks = tasks;
        this.server = server;
    }

    public void start() {
        tasks.repeating(this, Duration.ofMinutes(1));
    }

    @Override
    public void run() {
        if (changeService.needsSpawnChange()) {
            voteService.findCurrentlyWinningSpawn().ifPresent(this::forceNextSpawn);
        } else {
            long minutesUntilChange = changeService.findTimeUntilNextChange().toMinutes();
            if (minutesUntilChange == 5 || (minutesUntilChange % 15) == 0) {
                server.getOnlinePlayers().stream()
                        .filter(this::hasNotCastAVote)
                        .forEach(remindToCastAVote(minutesUntilChange));
            }
        }

        displayService.updateForAllPlayers();
    }

    private Consumer<Player> remindToCastAVote(long minutesUntilChange) {
        return player -> I18n.sendLoc(
                player, "spawn!vote.chat-reminder", Plurals.minutePlural(minutesUntilChange)
        );
    }

    private void forceNextSpawn(MapSpawn spawn) {
        spawns.forceNextSpawn(spawn);
        voteService.resetAllVotes();
        changeService.registerSpawnChange();
    }

    private boolean hasNotCastAVote(Player player) {
        return !voteService.findVote(player.getUniqueId()).isPresent();
    }
}
