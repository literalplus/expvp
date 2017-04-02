/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.service;

import li.l1t.common.exception.UserException;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

/**
 * A service that provides read/write access to skill metadata of players.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-08
 */
public interface ResearchService {
    /**
     * Researches given skill in given tree.
     *
     * @param player    the player to operate on
     * @param skillNode the node storing the skill to obtain
     * @throws UserException if the skill is already obtained, a required parent skill has not been researched, or
     *                       something else prevents obtainment
     */
    void research(Player player, SimpleSkillTreeNode skillNode);

    /**
     * Forgets a player's skill, removing it from their skill set.
     *
     * @param player the unique id of the player
     * @param skill  the skill to forget
     * @throws UserException if the skill has not been obtained
     */
    void forget(Player player, Skill skill);

    /**
     * Checks whether a player has obtained a skill.
     *
     * @param playerId the unique id of the player
     * @param skill    the skill to check
     * @return whether given player has obtained given skill
     */
    boolean has(UUID playerId, Skill skill);

    /**
     * @param playerId the unique id of the player
     * @return the collection of skill ids given player has obtained
     */
    Collection<String> getObtainedSkills(UUID playerId);
}
