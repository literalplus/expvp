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

package me.minotopia.expvp.spawn;

import com.google.common.base.Preconditions;
import li.l1t.common.misc.XyLocation;
import li.l1t.common.util.config.HashMapConfig;
import li.l1t.common.util.config.MapConfig;
import me.minotopia.expvp.api.spawn.MapSpawn;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a map spawn backed by a YAML data store.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-12
 */
public class YamlMapSpawn implements MapSpawn, ConfigurationSerializable {
    private static final String ID_PATH = "id";
    private static final String LOCATION_PATH = "loc";
    private static final String AUTHOR_PATH = "author";
    private final String id;
    private XyLocation location;
    private String author = "";

    public YamlMapSpawn(String id) {
        this.id = id;
    }

    public YamlMapSpawn(Map<String, Object> input) {
        MapConfig config = HashMapConfig.of(input);
        this.id = config.findString(ID_PATH).orElseThrow(IllegalArgumentException::new);
        this.location = config.findTyped(LOCATION_PATH, XyLocation.class).orElse(null);
        this.author = config.findString(AUTHOR_PATH).orElse("");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean hasLocation() {
        return location != null;
    }

    @Override
    public XyLocation getLocation() {
        return location;
    }

    @Override
    public void setLocation(XyLocation location) {
        Preconditions.checkNotNull(location, "location");
        this.location = location;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        Preconditions.checkNotNull(author, "author");
        this.author = author;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(ID_PATH, id);
        result.put(LOCATION_PATH, location);
        result.put(AUTHOR_PATH, author);
        return result;
    }

    @Override
    public String toString() {
        return "YamlMapSpawn " +
                "" + id +
                " at " + location +
                " by '" + author + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YamlMapSpawn)) return false;

        YamlMapSpawn that = (YamlMapSpawn) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
