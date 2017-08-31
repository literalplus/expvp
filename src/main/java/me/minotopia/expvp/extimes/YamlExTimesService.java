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

package me.minotopia.expvp.extimes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.extimes.ExTime;
import me.minotopia.expvp.api.extimes.ExTimesService;
import me.minotopia.expvp.api.extimes.SpecialExTime;
import me.minotopia.expvp.extimes.cfg.YamlExTimesConfig;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An ExTimes service backed by a YAML configuration. <p>This implementation has three types of times, special (specific
 * to a single date), week, and weekend. Week and weekend times are only taken into account at all if there are no
 * special times for that day.</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
@Singleton
public class YamlExTimesService implements ExTimesService {
    private final YamlExTimesConfig config;

    @Inject
    public YamlExTimesService(YamlExTimesConfig config) {
        this.config = config;
    }

    @Override
    public void cleanUp() {
        if (config.getSpecialTimes().removeIf(SpecialExTime::hasPassed)) {
            config.trySave();
        }
    }

    @Override
    public List<? extends ExTime> findTodaysTimes() {
        return findTimesFor(LocalDate.now());
    }

    @Override
    public List<? extends ExTime> findTomorrowsTimes() {
        return findTimesFor(LocalDate.now().plusDays(1));
    }

    @Override
    public List<? extends ExTime> findTimesFor(LocalDate date) {
        List<ExTime> specialTimes = config.getSpecialTimes().stream()
                .filter(time -> time.getApplicableDate().equals(date))
                .collect(Collectors.toList());
        if (!specialTimes.isEmpty()) {
            return specialTimes;
        } else if (isWeekend(date)) {
            return config.getWeekendTimes();
        } else {
            return config.getWeekTimes();
        }
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek().ordinal() >= DayOfWeek.FRIDAY.ordinal();
    }

    @Override
    public boolean isCurrentlyOnline() {
        LocalTime now = LocalTime.now();
        List<? extends ExTime> todaysTimes = findTodaysTimes();
        return todaysTimes.isEmpty() ||
                todaysTimes.stream().anyMatch(time -> time.contains(now));
    }

    @Override
    public Optional<LocalDateTime> findNextOnlineTime() {
        return findNextTimeToday()
                .map(Optional::of)
                .orElseGet(this::findNextTimeTomorrow);
    }

    private Optional<LocalDateTime> findNextTimeToday() {
        return findTodaysTimes().stream()
                .map(ExTime::getStart)
                .filter(this::hasNotYetPassed)
                .findFirst()
                .map(start -> LocalDateTime.of(LocalDate.now(), start));
    }

    private boolean hasNotYetPassed(LocalTime time) {
        return LocalTime.now().isBefore(time);
    }

    private Optional<LocalDateTime> findNextTimeTomorrow() {
        return findTomorrowsTimes().stream()
                .map(ExTime::getStart)
                .filter(this::hasNotYetPassed)
                .findFirst()
                .map(start -> LocalDateTime.of(LocalDate.now().plusDays(1), start));
    }
}
