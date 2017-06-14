/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.hit;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a single hit that a known culprit executed on a known victim.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-01
 */
public interface Hit {
    UUID getCulpritId();

    Instant getInstant();

    double getDamage();

    boolean isOlderThan(Duration duration);
}
