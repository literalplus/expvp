/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skilltree;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.Nameable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Represents the root of a skill tree with additional tree-related metadata that is not important
 * for the data structure, but for displaying it.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
public class SkillTree extends SimpleSkillTreeNode implements ConfigurationSerializable, Nameable {
    public static final String NAME_PATH = "name";
    public static final String ICON_PATH = "icon";
    public static final String BRANCHES_EXCLUSIVE_PATH = "b-excl";
    public static final String SLOT_ID_PATH = "slot-id";
    public static final String TREE_ID_PATH = "tree-id";

    static {
        ConfigurationSerialization.registerClass(SkillTree.class);
    }

    private final String treeId;

    private String name;
    private ItemStack icon;
    private boolean branchesExclusive;
    private int slotId;
    private int height;
    private int width;

    /**
     * Creates a new skill tree.
     *
     * @param id the unique string id of this tree
     */
    public SkillTree(String id) {
        super(null);
        this.treeId = id;
    }

    /**
     * <p>Deserialisation constructor for a whole tree.</p> Deserialises a skill tree node which was
     * serialised by {@link #serialize()} and all its child nodes.
     *
     * @param source the source map
     * @throws IllegalArgumentException if the source map doesn't contain both id and tree-id, or
     *                                  children is not a list, or source map doesn't contain name
     *                                  or icon, or icon is not an item stack
     */
    @SuppressWarnings("unchecked")
    SkillTree(Map<String, Object> source) {
        super(source, null);
        Preconditions.checkArgument(source.containsKey(TREE_ID_PATH), "no tree id specified");
        this.treeId = source.get(TREE_ID_PATH).toString();
        if (source.containsKey(NAME_PATH)) {
            setName(String.valueOf(source.get(NAME_PATH)));
        }
        Object iconObj = source.get(ICON_PATH);
        if (iconObj != null) {
            Preconditions.checkArgument(iconObj instanceof ItemStack, "icon must be an item stack: %s", iconObj);
            setIconStack((ItemStack) iconObj); // <--- unchecked
        }
        if (source.containsKey(BRANCHES_EXCLUSIVE_PATH)) {
            setBranchesExclusive((boolean) source.get(BRANCHES_EXCLUSIVE_PATH));
        }
        if (source.containsKey(SLOT_ID_PATH)) {
            setSlotId((Integer) source.get(SLOT_ID_PATH));
        }
    }

    /**
     * @return the human-readable name of this tree
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the human-readable name of this tree.
     *
     * @param name the new human-readable name of this tree
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the item icon of this tree, for use in inventories
     */
    public ItemStack getIconStack() {
        return icon;
    }

    /**
     * Sets the item icon of this tree, for use in inventories.
     *
     * @param icon the new item icon of this tree
     */
    public void setIconStack(ItemStack icon) {
        this.icon = icon;
    }


    /**
     * Gets the id of the slot this tree occupies in a inventory with as many slots as the maximum
     * slots of {@link org.bukkit.event.inventory.InventoryType#CHEST}.
     *
     * @return the id of the slot this tree occupies in a inventory
     */
    public int getSlotId() {
        return slotId;
    }

    /**
     * Sets the id of the slot this tree occupies in a inventory.
     *
     * @param slotId the id of the new slot this tree occupies in a inventory
     */
    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    /**
     * Gets whether branches are mutually exclusive in this tree, that means you can only research
     * one child per node.
     *
     * @return whether branches are mutually exclusive in this tree
     */
    public boolean areBranchesExclusive() {
        return branchesExclusive;
    }

    /**
     * Sets whether branches are {@link #areBranchesExclusive()} mutually exclusive} in this tree
     *
     * @param branchesExclusive whether branches are mutually exclusive in this tree
     */
    public void setBranchesExclusive(boolean branchesExclusive) {
        this.branchesExclusive = branchesExclusive;
    }

    /**
     * Gets the height of the tree, that is, the amount of children whose child ids are greater than
     * 0.
     *
     * @return the current height of the tree
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the width of the tree, that is, the length, in nodes, including the root node, of the
     * longest straight path to a leaf.
     *
     * @return the current width of the tree
     */
    public int getWidth() {
        return width;
    }

    void processChildAdd(SimpleSkillTreeNode child) {
        height = height + (child.getChildren().size() - 1);
        width = findChildWidth(child);
    }

    void processChildRemove(SimpleSkillTreeNode child) {
        height = height - (child.getChildren().size() - 1);
        int nodeWidth = child.getPosition().length + 1;
        if (width <= nodeWidth) {
            this.width = 1;
            forEachNode(node -> width = findChildWidth(child));
        }
    }

    private int findChildWidth(SimpleSkillTreeNode child) {
        return Math.max(width, child.getPosition().length + 1);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put(NAME_PATH, name);
        map.put(ICON_PATH, icon);
        map.put(BRANCHES_EXCLUSIVE_PATH, branchesExclusive);
        map.put(SLOT_ID_PATH, slotId);
        map.put(TREE_ID_PATH, treeId);
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillTree)) return false;
        SkillTree skills = (SkillTree) o;
        return getId().equals(skills.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public String getTreeId() {
        return treeId;
    }

    @Override
    public String getId() {
        return getTreeId();
    }
}
