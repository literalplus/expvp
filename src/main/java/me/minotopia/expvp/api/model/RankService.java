/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.model;

/**
 * Computes players' ranks in various categories.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-27
 */
public interface RankService {
    /**
     * Force-fetches a player's rank by Exp from the database, even if there is one cached.
     *
     * @param data the player to find the exp rank for
     * @return the amount of players that have more Exp than given player, plus one
     */
    long findExpRank(PlayerData data);

    /**
     * @param data the player to find the exp rank for
     * @return the {@link #findExpRank(PlayerData) fetched exp rank} for given player, or a short-term cached value, if
     * available
     */
    long getExpRank(PlayerData data);
}
