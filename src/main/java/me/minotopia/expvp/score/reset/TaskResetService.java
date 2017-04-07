/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.reset;

import me.minotopia.expvp.api.reset.ResetService;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Resets certain player data weekly using a scheduled task.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-07
 */
public class TaskResetService implements ResetService {
    @Override
    public DayOfWeek getResetDay() {
        return DayOfWeek.FRIDAY;
    }

    @Override
    public LocalTime getResetTime() {
        return LocalTime.of(1, 10);
    }
}
