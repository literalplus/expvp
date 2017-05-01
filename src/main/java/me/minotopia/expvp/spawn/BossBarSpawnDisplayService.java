/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.spawn;

import com.google.inject.Inject;
import li.l1t.common.i18n.Message;
import li.l1t.common.util.task.TaskService;
import me.confuser.barapi.BarAPI;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.api.spawn.SpawnChangeService;
import me.minotopia.expvp.api.spawn.SpawnDisplayService;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Displays spawn status using the boss bar.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-14
 */
@ConstructOnEnable
public class BossBarSpawnDisplayService implements SpawnDisplayService {
    private final SpawnService spawnService;
    private final SpawnChangeService spawnChangeService;
    private final Server server;
    private final DisplayNameService names;
    private final TaskService tasks;

    @Inject
    @SuppressWarnings("deprecation")
    public BossBarSpawnDisplayService(SpawnService spawnService, SpawnChangeService spawnChangeService,
                                      Server server, DisplayNameService names, PlayerInitService initService, TaskService tasks) {
        this.spawnService = spawnService;
        this.spawnChangeService = spawnChangeService;
        this.server = server;
        this.names = names;
        this.tasks = tasks;
        initService.registerInitHandler(player -> updateForAllPlayers());
        initService.registerDeInitHandler(BarAPI::removeBar); //reloads cause stale bars otherwise
    }

    @Override
    public void updateForAllPlayers() {
        if (spawnService.getCurrentSpawn().isPresent()) {
            updatePlayersBossBars();
        } else {
            resetAllBars();
        }
    }

    private void updatePlayersBossBars() {
        Message statusMessage = createStatusMessage();
        new ArrayList<Player>(server.getOnlinePlayers()).stream()
                .collect(Collectors.groupingBy(player -> I18n.getLocaleFor(player.getUniqueId())))
                .forEach(((locale, players) -> sendToAll(I18n.loc(locale, statusMessage), players)));
    }

    @SuppressWarnings("deprecation")
    private void sendToAll(String message, List<Player> players) {
        float fractionProgress = spawnChangeService.findFractionProgressToNextSpawn();
        players.forEach(player -> BarAPI.setMessage(player, message, fractionProgress * 100F));
    }

    @SuppressWarnings("deprecation")
    private void resetAllBars() {
        server.getOnlinePlayers().forEach(BarAPI::removeBar);
    }

    private Message createStatusMessage() {
        return Message.of("spawn!bossbar-format",
                findCurrentMapName(),
                findMinutesLeftUntilNextChange()
        );
    }

    private long findMinutesLeftUntilNextChange() {
        long timeLeftMinutes = spawnChangeService.findTimeUntilNextChange().toMinutes();
        return Math.max(timeLeftMinutes, 0);
    }

    private Message findCurrentMapName() {
        return spawnService.getCurrentSpawn().map(names::displayName).orElseThrow(IllegalStateException::new);
    }
}
