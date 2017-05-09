/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.extimes;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Represents a time range during which Expvp is available. The start and end times may be the same to sign that Expvp
 * is not available at all on that day, except if another range is specified.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
public interface ExTime {
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
