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
