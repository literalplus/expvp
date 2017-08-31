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
import java.time.LocalTime;
import java.util.List;

/**
 * Stores configured time ranges for a {@link ExTimesService} that is configurable.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
public interface ExTimesConfig {
    /**
     * @return the <i>modifiable</i> list of special times currently configured
     */
    List<? extends SpecialExTime> getSpecialTimes();

    SpecialExTime addSpecialTime(LocalDate date, LocalTime start, LocalTime end);

    /**
     * @return the <i>modifiable</i> list of weekend times currently configured
     */
    List<? extends DayOfWeekExTime> getWeekendTimes();

    DayOfWeekExTime addWeekendTime(LocalTime start, LocalTime end);

    /**
     * @return the <i>modifiable</i> list of week times currently configured
     */
    List<? extends DayOfWeekExTime> getWeekTimes();

    DayOfWeekExTime addWeekTime(LocalTime start, LocalTime end);

    boolean trySave();
}
