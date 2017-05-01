/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import li.l1t.common.intake.provider.annotation.Sender;
import li.l1t.common.util.LocationHelper;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.time.Duration;

/**
 * A command that teleports to spawn.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-07-23
 */
@AutoRegister("spawn")
public class CommandSpawn {
    private final SpawnService spawns;
    private final TaskService tasks;

    @Inject
    public CommandSpawn(SpawnService spawns, TaskService tasks) {
        this.spawns = spawns;
        this.tasks = tasks;
    }

    @Command(aliases = "",
            desc = "cmd!spawn.root.desc")
    public void newSkill(@Sender Player player)
            throws IOException {
        I18n.sendLoc(player, "core!spawn.stand-still");
        Location initialLocation = player.getLocation();
        double initialHealth = player.getHealth();
        int initialRemainingAir = player.getRemainingAir();
        tasks.delayed(() -> {
            if (!LocationHelper.softEqual(initialLocation, player.getLocation()) ||
                    player.getHealth() < initialHealth ||
                    player.getRemainingAir() < initialRemainingAir) {
                I18n.sendLoc(player, "core!spawn.tp-abort");
            } else {
                spawns.teleportToSpawnIfPossible(player);
            }
        }, Duration.ofSeconds(5));
    }
}
