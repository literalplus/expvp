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

package me.minotopia.expvp.api.score.points;

import me.minotopia.expvp.api.model.MutablePlayerData;
import org.bukkit.entity.Player;

/**
 * Figures out modification of Talent Points, and also provides the current amount of Talent Points of a player.
 * Notifies the player of the changes made, if any.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
public interface TalentPointService {
    int getCurrentTalentPointCount(Player player);

    /**
     * @param player the player to inspect
     * @return given player's overall Talent Point limit for the {@link TalentPointType#COMBAT} type
     * @deprecated Does not respect other types of Talent Points.
     */
    @Deprecated
    int findTalentPointLimit(Player player);

    /**
     * @param player the player to check
     * @return whether given player has reached their personal {@link #findTalentPointLimit(Player) Talent Point limit},
     * that means that they will not receive further Talent Points unless their kills get reset
     * @deprecated Does not respect other types of Talent Points besides {@link TalentPointType#COMBAT}
     */
    @Deprecated
    boolean hasReachedTalentPointLimit(Player player);

    /**
     * Grants given player the amount of Talent Points they deserve for given type.
     *
     * @param player the player to operate on
     * @param type   the type of Talent Points to compute and grant
     * @return the positive amount of Talent Points granted, zero if none
     * @see TalentPointTypeStrategy#findDeservedPoints(MutablePlayerData)
     */
    int grantDeservedTalentPoints(Player player, TalentPointType type);

    /**
     * Force-grants given player an amount of Talent Points off-the-record. This does not affect Talent Point limits.
     *
     * @param player       the player to operate on
     * @param talentPoints the amount of talent points to grant, may be negative or zero
     * @deprecated This does not affect Talent Point limits, and should not be used under normal circumstances
     */
    @Deprecated
    void grantTalentPoints(Player player, int talentPoints);

    /**
     * Consumes given amount of Talent Points from given player's account.
     *
     * @param player the player whose account to use
     * @param amount the amount if Talent Points to consume
     * @throws InsufficientTalentPointsException if given player does not have given amount of Talent Points
     */
    void consumeTalentPoints(Player player, int amount);

    /**
     * @param player the player to operate on
     * @param type the type of Talent Point to find the next point objective for
     * @return the next point objective for given type
     * @see TalentPointTypeStrategy#calculateObjectiveForNext(MutablePlayerData)
     */
    TalentPointObjective nextPointObjective(Player player, TalentPointType type);

}
