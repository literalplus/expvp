/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.i18n;

import li.l1t.common.intake.i18n.Message;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SkillTree;
import org.bukkit.entity.Player;

/**
 * Figures out display names and descriptions for different Expvp entities.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public interface DisplayNameService {
    /**
     * @param skill the skill to display
     * @return a message object representing given skill's display name
     */
    Message displayName(Skill skill);

    Message description(Skill skill);

    /**
     * @param tree the tree to display
     * @return a message object representing given tree's display name
     */
    Message displayName(SkillTree tree);

    Message description(SkillTree tree);

    /**
     * @param player the player whose display name to compute
     * @return a message object representing given player's display name
     */
    Message displayName(Player player);

    Message displayName(League league);

    Message displayName(MapSpawn spawn);
}
