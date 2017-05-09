/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
