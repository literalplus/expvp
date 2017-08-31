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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Handles making Expvp available only at specific times.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
public interface ExTimesService {
    /**
     * Cleans up time ranges that have already passed from this service.
     */
    void cleanUp();

    List<? extends ExTime> findTodaysTimes();

    List<? extends ExTime> findTomorrowsTimes();

    /**
     * @param date the date to target
     * @return the set of times that apply on given date
     */
    List<? extends ExTime> findTimesFor(LocalDate date);

    boolean isCurrentlyOnline();

    /**
     * @return an optional containing the next time the server will be available, excluding any currently applying
     * times, or, if the server won't be available today, the next time tomorrow, otherwise, an empty optional
     */
    Optional<LocalDateTime> findNextOnlineTime();
}
