/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.minotopia.expvp.api.model;

import me.minotopia.expvp.api.score.points.TalentPointType;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Represents persistent and mutable Expvp-related stats and metadata of a player.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-08
 */
public interface PlayerData extends Serializable {
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

    int getTotalKillAssists();

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
    int getAvailableTalentPoints();

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

    int getBestKillStreak();

    /**
     * Gets the set of skills this player has.
     *
     * @return the set of skills
     */
    Set<ObtainedSkill> getSkills();

    /**
     * @param type the type to retrieve the amount for
     * @return the total amount of Talent Points this player has been granted in given type since the last reset
     */
    int getTalentPointCount(TalentPointType type);
}
