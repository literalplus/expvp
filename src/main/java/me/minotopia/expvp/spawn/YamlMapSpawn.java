/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.spawn;

import com.google.common.base.Preconditions;
import li.l1t.common.misc.XyLocation;
import me.minotopia.expvp.api.spawn.MapSpawn;

/**
 * Represents a map spawn backed by a YAML data store.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-12
 */
public class YamlMapSpawn implements MapSpawn {
    private final String id;
    private XyLocation location;
    private String author = "";

    public YamlMapSpawn(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
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
    public String toString() {
        return "YamlMapSpawn " +
                "" + id +
                " at " + location +
                " by '" + author + '\'';
    }
}
