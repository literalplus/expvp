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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
        config.set(serializeLocation(location), spawn.getId());
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

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
    }

    public static XyLocation deserializeLocation(String input) {
        Preconditions.checkNotNull(input, "input");
        String[] parts = input.split("_");
        Preconditions.checkArgument(parts.length == 4, "expected 4 parts in %s", input);
        World world = Bukkit.getWorld(parts[0]);
        Preconditions.checkNotNull(world, "world with name %s from %s", parts[0], input);
        try {
            return new XyLocation(
                    world, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3])
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("argument not a number in " + input, e);
        }
    }
}
