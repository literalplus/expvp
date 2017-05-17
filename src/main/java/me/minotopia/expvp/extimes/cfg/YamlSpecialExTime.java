/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.extimes.cfg;

import com.google.common.base.Preconditions;
import li.l1t.common.util.config.HashMapConfig;
import li.l1t.common.util.config.MapConfig;
import me.minotopia.expvp.api.extimes.SpecialExTime;
import org.bukkit.configuration.serialization.SerializableAs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * A special available time represented as a YAML object.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
@SerializableAs("exp.ext.special")
public class YamlSpecialExTime extends AbstractExTime implements SpecialExTime {
    private final LocalDate applicableDate;

    public YamlSpecialExTime(Map<String, Object> source) {
        super(source);
        MapConfig config = HashMapConfig.of(source);
        this.applicableDate = config.findString("date")
                .map(LocalDate::parse)
                .orElseThrow(missingArgumentException("date"));
    }

    public YamlSpecialExTime(LocalDate applicableDate, LocalTime start, LocalTime end) {
        super(start, end);
        this.applicableDate = Preconditions.checkNotNull(applicableDate, "applicableDate");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = super.serialize();
        result.put("date", applicableDate.toString());
        return result;
    }

    @Override
    public LocalDate getApplicableDate() {
        return applicableDate;
    }

    @Override
    public boolean hasPassed() {
        return getApplicableDate().isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return super.toString() + " on " + applicableDate;
    }
}
