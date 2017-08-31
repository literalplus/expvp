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

package me.minotopia.expvp.api.extimes;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Represents a time range during which Expvp is available. The start and end times may be the same to sign that Expvp
 * is not available at all on that day, except if another range is specified.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
public interface ExTime {
    UUID getUniqueId();

    LocalTime getStart();

    LocalTime getEnd();

    Duration getDuration();

    /**
     * @return whether {@link #getDuration() the duration between start and end} is {@link Duration#isZero() is zero},
     * meaning that Expvp is not available on that day, except if another range is specified
     */
    boolean isEmpty();

    /**
     * @param time the time to check
     * @return whether this time range contains given time, assuming it is on the target day of this range
     */
    boolean contains(LocalTime time);
}
