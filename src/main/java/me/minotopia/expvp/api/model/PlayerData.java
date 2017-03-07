/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.model;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Represents persistent and mutable Expvp-related stats and metadata of a player.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-08
 */
public interface PlayerData {
    /**
     * @return the unique id of the player
     */
    UUID getUniqueId();

    /**
     * @return the amount of times this player has killed other players overall
     */
    int getTotalKills();

    /**
     * @return the amount of times this player has died overall
     */
    int getTotalDeaths();

    /**
     * @return the amount of times this player has killed other players since the last automated reset
     */
    int getCurrentKills();

    /**
     * @return the amount of times this player has died since the last automated reset
     */
    int getCurrentDeaths();

    /**
     * @return the internal name of the current league of this player
     */
    String getLeagueName();

    /**
     * Gets the amount of Exp this player currently has. Exp are calculated from kills and deaths based on the current
     * league of this player and the players they kill.
     *
     * @return the amount of Exp the player currently has
     */
    int getExp();

    /**
     * Gets the amount of talent points this player currently has. These may be used to obtain skills from a
     * skill tree and are obtained by killing other players.
     *
     * @return the amount of talent points this player currently has
     */
    int getTalentPoints();

    /**
     * Gets the locale preferred by the player. This can either be automatically computed from the player's client
     * settings, or explicitly set by the player. In the latter case, {@link #hasCustomLocale()} returns true.
     *
     * @return the locale preferred by the player
     */
    Locale getLocale();

    /**
     * @return whether this player has explicitly requested their current locale, making it so that client settings will
     * not override their locale
     */
    boolean hasCustomLocale();

    /**
     * Gets the set of skills this player has.
     *
     * @return the set of skills
     */
    Set<ObtainedSkill> getSkills();
}
