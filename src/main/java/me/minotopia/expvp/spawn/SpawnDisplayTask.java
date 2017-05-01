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
