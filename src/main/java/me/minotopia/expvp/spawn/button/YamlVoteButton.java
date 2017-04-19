/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.spawn.button;

import com.google.common.base.Preconditions;
import li.l1t.common.misc.XyLocation;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.button.VoteButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * Stores data about a physical vote button that may be used to cast a vote for a spawn.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-19
 */
@SerializableAs("exp.vote-button")
public class YamlVoteButton implements VoteButton {
    private final XyLocation location;
    private MapSpawn spawn;

    public YamlVoteButton(XyLocation location, MapSpawn spawn) {
        this.location = Preconditions.checkNotNull(location);
        this.spawn = Preconditions.checkNotNull(spawn);
    }

    public void saveTo(YamlConfiguration config) {
        config.set(location.serializeToString(), spawn.getId());
    }

    @Override
    public XyLocation getLocation() {
        return location;
    }

    @Override
    public MapSpawn getSpawn() {
        return spawn;
    }

    @Override
    public void setSpawn(MapSpawn spawn) {
        this.spawn = spawn;
    }

    @Override
    public String toString() {
        return "YamlVoteButton " +
                " at " + location +
                " for '" + spawn + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YamlVoteButton)) return false;
        YamlVoteButton that = (YamlVoteButton) o;
        return location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }
}
