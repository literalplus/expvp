/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.spawn.button;

import li.l1t.common.misc.XyLocation;
import me.minotopia.expvp.api.spawn.MapSpawn;

/**
 * Stores data about a physical vote button that may be used to cast a vote for a spawn.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-19
 */
public interface VoteButton {
    XyLocation getLocation();

    MapSpawn getSpawn();

    void setSpawn(MapSpawn spawn);
}
