/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.model;

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
    int getKills();

    /**
     * @return the amount of times this player has died
     */
    int getDeaths();

    /**
     * @return the current level of this player, starting at 1
     */
    int getLevel();

    /**
     * Gets the amount of points this player currently has for progressing to the next level. Points
     * are increased each time the player kills somebody and decreased every time the player dies.
     * Upon reaching a certain amount of points, the player progresses to the next level.
     *
     * @return the amount of points the player currently has
     */
    int getPoints();

    /**
     * Gets the amount of books this player currently has. Books can be used to obtain skills from a
     * skill tree and are added at level-up, except if the player has reached their book limit.
     *
     * @return the current amount of books this player has
     */
    int getBooks();

    /**
     * Gets the amount of melons this player has. Melons are a premium currency used for purchasing
     * cosmetic things.
     *
     * @return the current amount of melons this player has
     */
    int getMelons();

    /**
     * Gets the set of skills this player has.
     *
     * @return the set of skills
     */
    Set<ObtainedSkill> getSkills();
}
