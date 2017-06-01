/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.model;

import me.minotopia.expvp.api.score.TalentPointType;

/**
 * Represents a player's current Talent Point count for a specific type. These are used to implement limits per type.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-01
 */
public interface PlayerPoints {
    PlayerData getPlayerData();

    TalentPointType getType();

    int getCurrentPointCount();

    void increasePointCountBy(int modifier);
}
