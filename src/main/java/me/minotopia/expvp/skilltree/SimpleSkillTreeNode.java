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

package me.minotopia.expvp.skilltree;

import com.google.common.base.Preconditions;
import li.l1t.common.tree.AbstractTreeNode;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simple implementation of a skill tree node. This implementation notifies the root node when children are added or
 * removed, so subclasses must make sure to always call the super method from {@link #addChild(SimpleSkillTreeNode)} and
 * {@link #removeChild(SimpleSkillTreeNode)}. <p><b>Note:</b> This class does not implement {@link
 * org.bukkit.configuration.serialization.ConfigurationSerializable} because of its limitations. Nodes must be manually
 * serialized and deserialized from a map. The only correct way to serialise a complete skill tree is to serialise the
 * root {@link SkillTree}. Also note that deserialising part of a skill tree is <b>not</b> supported: That would mess up
 * tree ids.</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-23
 */
public class SimpleSkillTreeNode extends AbstractTreeNode<SimpleSkillTreeNode, Skill>
        implements SkillTreeNode<SimpleSkillTreeNode> {
    private static final String SKILL_ID_PATH = "skill-id";
    private static final String CHILDREN_PATH = "children";
    private String skillId;
    private SkillTree tree;

    /**
     * Creates a new skill tree node. If parent is null, creates a skill tree. Note that there is no
     * practical difference between a skill tree and a skill tree node.
     *
     * @param parent the parent of this node
     */
    SimpleSkillTreeNode(SimpleSkillTreeNode parent) {
        super(parent, SimpleSkillTreeNode.class);
        if (parent != null) {
            this.tree = parent.getTree();
        } else if (this instanceof SkillTree) {
            this.tree = (SkillTree) this;
        } else {
            throw new IllegalArgumentException("only SkillTree instances need not have a parent");
        }
    }

    /**
     * Deserialises a skill tree node which was serialised by {@link #serialize()} and all its child
     * nodes.
     *
     * @param source the source map
     * @param parent the parent of the node, or null for a root node
     * @throws IllegalArgumentException if the source map doesn't contain both id and tree-id, children is not a list
     */
    @SuppressWarnings("unchecked")
    SimpleSkillTreeNode(Map<String, Object> source, SimpleSkillTreeNode parent) {
        this(parent);
        if (parent != null) {
            parent.addChild(this); //prevent parent complaining because we already have children after constructor
        }
        if (source.containsKey(SKILL_ID_PATH)) {
            this.skillId = String.valueOf(source.get(SKILL_ID_PATH));
        }
        if (source.containsKey(CHILDREN_PATH)) {
            Object childrenObj = source.get(CHILDREN_PATH);
            Preconditions.checkArgument(childrenObj instanceof List, "children must be a list");
            List<Object> childrenList = (List<Object>) childrenObj; // <-- unchecked
            childrenList.stream()
                    .filter(el -> el instanceof Map)
                    .forEach(map -> new SimpleSkillTreeNode((Map<String, Object>) map, this)); //<-- unchecked
            //constructor already adds children to parent (that's us!)
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
    public SimpleSkillTreeNode createChild() {
        SimpleSkillTreeNode node = new SimpleSkillTreeNode(this);
        addChild(node);
        return node;
    }

    @Override
    public boolean matches(ObtainedSkill modelSkill) {
        return getValue() != null && getValue().getId().equals(modelSkill.getSkillId());
    }

    @Override
    public boolean matches(Collection<ObtainedSkill> modelSkills) {
        return modelSkills.stream().anyMatch(this::matches);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put(CHILDREN_PATH,
                getChildren().stream()
                        .map(SkillTreeNode::serialize)
                        .collect(Collectors.toList())
        );
        map.put(SKILL_ID_PATH, skillId);
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

    @Override
    public SkillTree getTree() {
        return tree;
    }

    @Override
    public String getSkillId() {
        return skillId;
    }

    @Override
    public void setValue(Skill newValue) {
        super.setValue(newValue);
        if (newValue == null) {
            skillId = null;
        } else {
            skillId = newValue.getId();
        }
    }
}
