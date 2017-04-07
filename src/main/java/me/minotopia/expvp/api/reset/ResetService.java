/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.reset;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Service for handling weekly resets of certain user data.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-07
 */
public interface ResetService { //TODO: *Actual* reset handling #744
    DayOfWeek getResetDay();

    LocalTime getResetTime();
}
