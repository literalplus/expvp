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

package me.minotopia.expvp.api.spawn;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Manages automatic spawn changes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-14
 */
public interface SpawnChangeService {
    boolean needsSpawnChange();

    LocalDateTime findNextSpawnChangeTime();

    LocalDateTime findLastSpawnChangeTime();

    Duration findTimeUntilNextChange();

    /**
     * @return the fractional progress between the last and next spawn change, meaning a result of 1.0 if the spawn
     * change would occur now and 0.01 if the spawn was just changed, but never less than zero and never more than 1.0
     */
    float findFractionProgressToNextSpawn();

    void registerSpawnChange();
}
