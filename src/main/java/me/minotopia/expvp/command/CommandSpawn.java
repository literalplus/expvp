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
import me.minotopia.expvp.api.handler.kit.KitService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.i18n.Format;
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
    private final KitService kitService;

    @Inject
    public CommandSpawn(SpawnService spawns, TaskService tasks, EPPlugin plugin, KitService kitService) {
        this.spawns = spawns;
        this.tasks = tasks;
        this.kitService = kitService;
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
        I18n.sendLoc(player, Format.result("core!spawn.stand-still"));
        Location initialLocation = player.getLocation();
        double initialHealth = player.getHealth();
        int initialRemainingAir = player.getRemainingAir();
        tasks.delayed(() -> {
            if (!LocationHelper.softEqual(initialLocation, player.getLocation()) ||
                    player.getHealth() < initialHealth ||
                    player.getRemainingAir() < initialRemainingAir) {
                I18n.sendLoc(player, Format.userError("core!spawn.tp-abort"));
            } else {
                kitService.applyKit(player);
                spawns.teleportToSpawnIfPossible(player);
            }
        }, Duration.ofSeconds(5));
        return true;
    }
}
