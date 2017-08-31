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
import me.minotopia.expvp.api.spawn.SpawnChangeService;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * A spawn change service that schedules spawn changes every full hour.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-14
 */
@Singleton
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
    public float findFractionProgressToNextSpawn() {
        LocalDateTime lastChange = findLastSpawnChangeTime();
        Duration fullDuration = Duration.between(lastChange, findNextSpawnChangeTime());
        Duration passedDuration = Duration.between(lastChange, LocalDateTime.now());
        float result = ((float) passedDuration.getSeconds()) / ((float) fullDuration.getSeconds());
        result += 0.01F;
        return result > 1F ? 1.0F : result;
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
