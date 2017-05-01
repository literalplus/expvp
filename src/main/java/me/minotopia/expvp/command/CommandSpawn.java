/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.google.inject.Inject;
import li.l1t.common.command.BukkitExecution;
import li.l1t.common.command.BukkitExecutionExecutor;
import li.l1t.common.exception.InternalException;
import li.l1t.common.exception.UserException;
import li.l1t.common.util.LocationHelper;
import li.l1t.common.util.task.TaskService;
import li.l1t.xlogin.common.api.XLoginProfile;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

/**
 * A command that teleports to spawn.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-07-23
 */
@ConstructOnEnable
public class CommandSpawn extends BukkitExecutionExecutor {
    private static final Logger LOGGER = LoggingManager.getLogger(CommandSpawn.class);
    private final SpawnService spawns;
    private final TaskService tasks;

    @Inject
    public CommandSpawn(SpawnService spawns, TaskService tasks, EPPlugin plugin) {
        this.spawns = spawns;
        this.tasks = tasks;
        plugin.getCommand("spawn").setExecutor(this);
        overrideXLoginSpawnCommand(plugin);
    }

    private void overrideXLoginSpawnCommand(EPPlugin plugin) {
        try {
            if (plugin.getConfig().getBoolean("override-xlogin-spawn-command-because-bukkits-command-handling-sucks", true)) {
                JavaPlugin xLogin = JavaPlugin.getProvidingPlugin(XLoginProfile.class);
                if (xLogin != null) {
                    LOGGER.debug("Overriding xLogin's /spawn because Bukkit's command registration sucks...");
                    xLogin.getCommand("spawn").setExecutor(this);
                }
            }
        } catch (Exception e) {
            LOGGER.info("Failed to override xLogin's /spawn", e);
        }
    }

    @Override
    public boolean execute(BukkitExecution exec) throws UserException, InternalException {
        Player player = exec.player();
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
        return true;
    }
}
