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

package me.minotopia.expvp.spawn;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.handler.kit.KitService;
import me.minotopia.expvp.api.i18n.DisplayNameService;
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
    private final DisplayNameService names;
    private final KitService kitService;
    private long lastReminderMinute;

    @Inject
    public SpawnVoteTask(SpawnDisplayService displayService, SpawnService spawns,
                         SpawnChangeService changeService, SpawnVoteService voteService,
                         TaskService tasks, Server server, DisplayNameService names, KitService kitService) {
        this.displayService = displayService;
        this.spawns = spawns;
        this.changeService = changeService;
        this.voteService = voteService;
        this.server = server;
        this.names = names;
        this.kitService = kitService;
        tasks.repeating(this, Duration.ofSeconds(30));
    }

    @Override
    public void run() {
        if (changeService.needsSpawnChange()) {
            voteService.findCurrentlyWinningSpawn().ifPresent(this::forceNextSpawn);
        } else {
            long minutesUntilChange = changeService.findTimeUntilNextChange().toMinutes();
            if (minutesUntilChange > 0 && isNotificationMinute(minutesUntilChange)) {
                remindEligiblePlayersToVote(minutesUntilChange);
            }
        }
    }

    private boolean isNotificationMinute(long minutesUntilChange) {
        return minutesUntilChange == 1 || minutesUntilChange == 5 || (minutesUntilChange % 15) == 0;
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
        return player -> {
            if (!voteService.getCurrentVotes().isEmpty()) {
                voteService.showCurrentVotesTo(player, false);
            }
            I18n.sendLoc(
                    player, Format.broadcast("spawn!vote.chat-reminder", Plurals.minutePlural(minutesUntilChange))
            );
        };
    }

    private void forceNextSpawn(MapSpawn spawn) {
        server.getOnlinePlayers().forEach(player -> notifyNextSpawn(spawn, player));
        spawns.forceNextSpawn(spawn);
        voteService.resetAllVotes();
        changeService.registerSpawnChange();
        server.getOnlinePlayers().stream()
                .peek(kitService::applyKit)
                .forEach(spawns::teleportToSpawnIfPossible);
        displayService.updateForAllPlayers();
    }

    private void notifyNextSpawn(MapSpawn spawn, Player player) {
        voteService.showCurrentVotesTo(player, false);
        I18n.sendLoc(player, "core!spawn.new-spawn", names.displayName(spawn), spawn.getAuthor());
    }

    private boolean hasNotCastAVote(Player player) {
        return !voteService.findVote(player.getUniqueId()).isPresent();
    }
}
