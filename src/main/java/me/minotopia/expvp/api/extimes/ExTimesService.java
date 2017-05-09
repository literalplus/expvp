/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.extimes;

import java.time.LocalDate;
import java.util.List;

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
}
