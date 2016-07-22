/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree;

import com.google.common.base.Preconditions;
import li.l1t.common.tree.SimpleTreeNode;
import me.minotopia.expvp.model.player.ObtainedSkill;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simple implementation of a skill tree node. This implementation notifies the root node when
 * children are added or removed, so subclasses must make sure to always call the super method
 * from {@link #addChild(SimpleSkillTreeNode)} and {@link #removeChild(SimpleSkillTreeNode)}.
 * <p><b>Note:</b> This class does not implement {@link org.bukkit.configuration.serialization.ConfigurationSerializable}
 * because of its limitations. Nodes must be manually serialized and deserialized from a map.
 * The only correct way to serialise a complete skill tree is to serialise the root
 * {@link SkillTree}. Also note that deserialising part of a skill tree is <b>not</b> supported:
 * That would mess up tree ids.</p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
public class SimpleSkillTreeNode extends SimpleTreeNode<SimpleSkillTreeNode, Skill>
        implements SkillTreeNode<SimpleSkillTreeNode> {
    public static final String ID_PATH = "id";
    public static final String CHILDREN_PATH = "children";

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
    public SimpleSkillTreeNode(SimpleSkillTreeNode parent, String id) {
        super(parent, SimpleSkillTreeNode.class);
        Preconditions.checkNotNull(id, "id");
        if (parent == null) {
            this.treeId = id;
        } else {
            this.treeId = parent.getTreeId();
        }
        this.id = id;
    }

    /**
     * Deserialises a skill tree node which was serialised by {@link #serialize()} and all its
     * child nodes.
     *
     * @param source the source map
     * @param parent the parent of the node, or null for a root node
     * @throws IllegalArgumentException if the source map doesn't contain both id and tree-id,
     *                                  children is not a list
     */
    @SuppressWarnings("unchecked")
    SimpleSkillTreeNode(Map<String, Object> source, SimpleSkillTreeNode parent) {
        this(parent, (String) source.get(ID_PATH));
        SimpleSkillTreeNode node = new SimpleSkillTreeNode(parent, id);
        if (source.containsKey(CHILDREN_PATH)) {
            Object childrenObj = source.get(CHILDREN_PATH);
            Preconditions.checkArgument(childrenObj instanceof List, "children must be a list");
            List<Object> childrenList = (List<Object>) childrenObj; // <-- unchecked
            childrenList.stream()
                    .filter(el -> el instanceof Map)
                    .map(map -> new SimpleSkillTreeNode((Map<String, Object>) map, node)) //<-- unchecked
                    .forEach(node::addChild);
        }
    }

    @Override
    public void addChild(SimpleSkillTreeNode newChild) {
        Preconditions.checkArgument(!newChild.hasChildren(), "new child must not have children!");
        super.addChild(newChild);
        processChildAdd(newChild);
    }

    @Override
    public void removeChild(SimpleSkillTreeNode oldChild) {
        oldChild.getChildren().forEach(grandchild -> grandchild.getParent().removeChild(oldChild));
        super.removeChild(oldChild);
        processChildRemove(oldChild);
    }

    @Override
    public SimpleSkillTreeNode createChild(String id) {
        SimpleSkillTreeNode node = new SimpleSkillTreeNode(this, id);
        addChild(node);
        return node;
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
        return id.equals(modelSkill.getSkillId());
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
        map.put(ID_PATH, id);
        map.put(CHILDREN_PATH,
                getChildren().stream()
                        .map(SkillTreeNode::serialize)
                        .collect(Collectors.toList())
        );
        return map;
    }

    // used to keep statistics up to date
    void processChildAdd(SimpleSkillTreeNode child) {
        if (getParent() != null) {
            getParent().processChildAdd(child);
        }
    }

    void processChildRemove(SimpleSkillTreeNode child) {
        if (getParent() != null) {
            getParent().processChildRemove(child);
        }
    }
}
