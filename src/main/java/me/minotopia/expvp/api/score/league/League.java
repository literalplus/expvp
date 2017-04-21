/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.league;

import org.bukkit.Material;

import java.util.Optional;

/**
 * Represents a class of players that players are sorted into by their Exp.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-29
 */
public interface League {
    String name();

    int getMinExp();

    int getKillExpReward();

    int getDeathExpPenalty();

    Material getDisplayMaterial();

    /**
     * @return the next league higher than this league
     */
    Optional<League> next();

    /**
     * @return the previous league lower than this league
     */
    Optional<League> previous();
}
