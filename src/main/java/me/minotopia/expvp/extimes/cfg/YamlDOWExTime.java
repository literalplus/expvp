/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.extimes.cfg;

import me.minotopia.expvp.api.extimes.DayOfWeekExTime;
import org.bukkit.configuration.serialization.SerializableAs;

import java.time.LocalTime;
import java.util.Map;

/**
 * A day-of-week ExTime backed by a YAML configuration.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
@SerializableAs("exp.ext.dow")
public class YamlDOWExTime extends AbstractExTime implements DayOfWeekExTime {
    public YamlDOWExTime(Map<String, Object> source) {
        super(source);
    }

    public YamlDOWExTime(LocalTime start, LocalTime end) {
        super(start, end);
    }
}
