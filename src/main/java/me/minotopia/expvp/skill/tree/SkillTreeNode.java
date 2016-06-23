/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree;

import me.minotopia.expvp.model.player.ObtainedSkill;
import me.minotopia.expvp.skill.Skill;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import io.github.xxyy.common.tree.TreeNode;

import java.util.Collection;

/**
 * Represents a node in a skill tree.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
public interface SkillTreeNode extends TreeNode<Skill>, ConfigurationSerializable {
    /**
     * @return the unique identifier of the tree this node belongs to
     */
    String getTreeId();

    /**
     * @return the unique identifier of this node
     */
    String getId();

    /**
     * Checks whether given model skill represents this node.
     *
     * @param modelSkill the model skill to check
     * @return whether given model skill represents this node
     */
    boolean matches(ObtainedSkill modelSkill);

    /**
     * Checks whether any of the given skills represents this node.
     *
     * @param modelSkills the model skills to check
     * @return whether any of the given model skills represents this node
     */
    boolean matches(Collection<ObtainedSkill> modelSkills);
}
