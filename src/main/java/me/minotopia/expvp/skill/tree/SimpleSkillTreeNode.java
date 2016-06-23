/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.model.player.ObtainedSkill;
import me.minotopia.expvp.skill.Skill;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import io.github.xxyy.common.tree.SimpleTreeNode;
import io.github.xxyy.common.tree.TreeNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simple implementation of a skill tree node.
 * <p><b>Note:</b> When serialising tree nodes in a configuration, make sure to know that only
 * children of the node are serialised, not the parent. That means that the only correct way to
 * serialise a complete skill tree is to serialise the root node. Also note that
 * deserialising part of a skill tree is <b>not</b> supported: That would mess up tree ids.</p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
public class SimpleSkillTreeNode extends SimpleTreeNode<Skill>
        implements SkillTreeNode, ConfigurationSerializable {
    static {
        ConfigurationSerialization.registerClass(SimpleSkillTreeNode.class);
    }

    private final String treeId;
    private final String id;

    /**
     * Creates a new skill tree node. If parent is null, creates a skill tree. Note that there is
     * no practical difference between a skill tree and a skill tree node, except that for the
     * skill tree, {@link #getTreeId() tree id} and {@link #getId() id} are the same.
     *
     * @param parent the parent of this node
     * @param id     the unique string id of this node
     */
    public SimpleSkillTreeNode(SkillTreeNode parent, String id) {
        super(parent);
        if (parent == null) {
            this.treeId = id;
        } else {
            this.treeId = parent.getTreeId();
        }
        this.id = id;
    }

    @Override
    public void addChild(TreeNode<Skill> newChild) {
        Preconditions.checkArgument(newChild instanceof SkillTreeNode,
                "newChild must be a SkillTreeNode, is: %s", newChild.getClass().getName());
        super.addChild(newChild);
    }

    @Override
    public String getTreeId() {
        return treeId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean matches(ObtainedSkill modelSkill) {
        return treeId.equals(modelSkill.getSkillTree()) &&
                id.equals(modelSkill.getSkillId());
    }

    @Override
    public boolean matches(Collection<ObtainedSkill> modelSkills) {
        return modelSkills.stream()
                .filter(this::matches)
                .findAny()
                .isPresent();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("tree-id", treeId); //Getting this by reverse iteration *is* possible, but terribly inefficient
        map.put("children",
                getChildren().stream()
                        .map(node -> ((SkillTreeNode) node).serialize())
                        .collect(Collectors.toList())
        );
        return map;
    }
}
