/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.spawn;

import com.google.inject.Inject;
import me.minotopia.expvp.api.spawn.SpawnChangeService;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * A spawn change service that schedules spawn changes every full hour.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-14
 */
public class FullHourSpawnChangeService implements SpawnChangeService {
    private LocalDateTime lastChangeTime;
    private LocalDateTime nextChangeTime;

    @Inject
    public FullHourSpawnChangeService() {
        setLastChangeTime(LocalDateTime.now());
    }

    private LocalDateTime roundToNextFullHour(LocalDateTime input) {
        return input.plusHours(1).withMinute(0).withSecond(0).withNano(0);
    }

    @Override
    public boolean needsSpawnChange() {
        Duration timeLeft = findTimeUntilNextChange();
        return timeLeft.isNegative() || timeLeft.isZero();
    }

    @Override
    public Duration findTimeUntilNextChange() {
        return Duration.between(LocalDateTime.now(), nextChangeTime);
    }

    @Override
    public LocalDateTime findNextSpawnChangeTime() {
        return nextChangeTime;
    }

    @Override
    public LocalDateTime findLastSpawnChangeTime() {
        return lastChangeTime;
    }

    @Override
    public void registerSpawnChange() {
        setLastChangeTime(LocalDateTime.now());
    }

    private void setLastChangeTime(LocalDateTime lastChange) {
        this.lastChangeTime = lastChange;
        this.nextChangeTime = roundToNextFullHour(lastChange);
    }
}
