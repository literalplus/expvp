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
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.spawn.SpawnDisplayService;

import java.time.Duration;

/**
 * Task that periodically updates players' boss bars.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-01
 */
@Singleton
@ConstructOnEnable
public class SpawnDisplayTask implements Runnable {
    private final SpawnDisplayService displayService;

    @Inject
    public SpawnDisplayTask(SpawnDisplayService displayService, TaskService tasks) {
        this.displayService = displayService;
        tasks.repeating(this, Duration.ofSeconds(1));
    }

    @Override
    public void run() {
        displayService.updateForAllPlayers();
    }
}
