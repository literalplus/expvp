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
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.spawn.SpawnChangeService;
import org.bukkit.Server;
import org.bukkit.World;

import java.time.Duration;

/**
 * A task that updates the in-game time based on the current fractional progress to the next spawn
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-23
 */
@ConstructOnEnable
public class SpawnDayTimeTask implements Runnable {
    private final Server server;
    private final SpawnChangeService spawnChangeService;

    @Inject
    public SpawnDayTimeTask(Server server, SpawnChangeService spawnChangeService, TaskService tasks) {
        this.server = server;
        this.spawnChangeService = spawnChangeService;
        tasks.repeating(this, Duration.ofSeconds(1));
    }

    @Override
    public void run() {
        server.getWorlds().forEach(this::setWorldTimeBasedOnProgress);
    }

    private void setWorldTimeBasedOnProgress(World world) {
        world.setTime(findDayTimeForSpawnProgress(spawnChangeService.findFractionProgressToNextSpawn()));
    }

    private int findDayTimeForSpawnProgress(float fractionProgress) {
        return Math.round(22_500F + (fractionProgress * 17520F));
    }
}
