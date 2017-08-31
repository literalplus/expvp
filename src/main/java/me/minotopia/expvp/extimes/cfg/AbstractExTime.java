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

package me.minotopia.expvp.extimes.cfg;

import com.google.common.base.Preconditions;
import li.l1t.common.util.config.HashMapConfig;
import li.l1t.common.util.config.MapConfig;
import me.minotopia.expvp.api.extimes.ExTime;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Abstract base class for available time ranges.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
public class AbstractExTime implements ConfigurationSerializable, ExTime {
    private final LocalTime start;
    private final LocalTime end;
    private final UUID uniqueId;

    public AbstractExTime(Map<String, Object> source) {
        MapConfig config = HashMapConfig.of(source);
        this.start = config.findString("start")
                .map(LocalTime::parse)
                .orElseThrow(missingArgumentException("start"));
        this.end = config.findString("end")
                .map(LocalTime::parse)
                .orElseThrow(missingArgumentException("end"));
        this.uniqueId = config.findString("uuid")
                .map(UUID::fromString)
                .orElseThrow(missingArgumentException("uuid"));
        Preconditions.checkArgument(!start.isAfter(end), "start must be before or equal to end", start, end);
    }

    public AbstractExTime(LocalTime start, LocalTime end) {
        this.uniqueId = UUID.randomUUID();
        this.start = Preconditions.checkNotNull(start, "start");
        this.end = Preconditions.checkNotNull(end, "end");
        Preconditions.checkArgument(!start.isAfter(end), "start must be before or equal to end", start, end);
    }

    protected Supplier<IllegalArgumentException> missingArgumentException(final String description) {
        return () -> new IllegalArgumentException("Missing " + description);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("start", start.toString());
        map.put("end", end.toString());
        map.put("uuid", uniqueId.toString());
        return map;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public Duration getDuration() {
        return Duration.between(start, end);
    }

    public boolean isEmpty() {
        return getDuration().isZero();
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public boolean contains(LocalTime time) {
        return time.isAfter(start) && time.isBefore(end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractExTime)) return false;
        AbstractExTime that = (AbstractExTime) o;
        return uniqueId.equals(that.uniqueId);
    }

    @Override
    public int hashCode() {
        return uniqueId.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + " from " + getStart() + " to " + getEnd();
    }
}
