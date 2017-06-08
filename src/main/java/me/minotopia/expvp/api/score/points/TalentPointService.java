/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.points;

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

    int findTalentPointLimit(Player player);

    /**
     * @param player the player to check
     * @return whether given player has reached their personal {@link #findTalentPointLimit(Player) Talent Point limit},
     * that means that they will not receive further Talent Points unless their kills get reset
     */
    boolean hasReachedTalentPointLimit(Player player);

    /**
     * Grants given player the talent points they deserve for their latest kill. This works by figuring out how many
     * Talent Points the player deserves overall for their current kill count, and then working out the difference
     * to the deserved Talent Points for the kill count before the kill. The difference in Talent Points is granted.
     *
     * @param player the player to operate on
     * @return the amount of Talent Points granted, zero if none
     */
    int grantTalentPointsForKill(Player player);

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
     * Finds the amount of kills given player needs until they are granted the next Talent Point.
     * <p><b>Note:</b> This method operates theoretically. It does not take into account the Talent Point limit.</p>
     *
     * @param player the player to operate on
     * @return the amount of kills left
     */
    int findKillsUntilNextTalentPoint(Player player);

    /**
     * Communicates to the player their current amount of Talent Points and how many kills they need until their next
     * Talent Point, via some means other than the chat.
     *
     * @param player the player to operate on
     */
    void displayCurrentCount(Player player);
}
