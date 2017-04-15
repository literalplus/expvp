/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.spawn;

/**
 * Displays information related to spawns to all players using some other means than the text chat
 * primarily.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-14
 */
public interface SpawnDisplayService {
    void updateForAllPlayers();
}
