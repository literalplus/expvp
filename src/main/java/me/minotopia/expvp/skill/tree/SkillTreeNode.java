/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree;

import li.l1t.common.tree.TreeNode;
import me.minotopia.expvp.model.player.ObtainedSkill;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a node in a skill tree.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
public interface SkillTreeNode<N extends SkillTreeNode<N>> extends TreeNode<N, Skill> {
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

    /**
     * Serialises this node and all its children to a map.
     *
     * @return a map representing this node and its children
     */
    Map<String, Object> serialize();

    /**
     * <p><b>Note:</b> Addition of nodes which already have children themselves is not permitted.</p>
     * <p><b>Note:</b> The preferred API way to add a child is to {@link #createChild(String)
     * let the node create it}.</p>
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the new child already has children
     */
    @Override
    void addChild(N newChild);

    /**
     * Preferred API way to add a child, outsourcing construction to the node itself, since it
     * knows best what's best for its children.
     *
     * @param id the string id of the new child node
     * @return the created child
     */
    N createChild(String id);
}
