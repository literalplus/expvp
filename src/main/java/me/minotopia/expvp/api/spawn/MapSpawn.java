/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.spawn;

import li.l1t.common.misc.XyLocation;
import me.minotopia.expvp.api.Identifiable;

/**
 * Represents the spawn point of a map that may be used with Expvp.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-12
 */
public interface MapSpawn extends Identifiable {
    XyLocation getLocation();

    void setLocation(XyLocation newLocation);

    String getAuthor();

    void setAuthor(String newAuthor);
}
