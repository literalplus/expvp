/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.meta;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.Identifiable;
import me.minotopia.expvp.api.model.ObtainedSkill;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the metadata of an obtainable skill. Note that skills are pluggable, so they do not
 * keep a reference to the skill tree they're part of - they can also be part of multiple trees.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-23
 */
public class Skill implements ConfigurationSerializable, Identifiable {
    private static final String ID_PATH = "id";
    private static final String BOOK_COST_PATH = "cost";
    private static final String ICON_PATH = "icon";
    private static final String HANDLER_SPEC_PATH = "handler";
    private SkillManager manager;
    private final String id;
    private int bookCost = 1; //sane default value
    private ItemStack iconStack;
    private String handlerSpec;

    /**
     * Creates a new skill.
     *
     * @param id the unique id for the new skill
     */
    public Skill(String id) {
        this.id = id;
    }

    /**
     * @return the manager that has claimed this skill, or null if none yet
     */
    public SkillManager getManager() {
        return manager;
    }

    /**
     * Permanently associates this skill with a manager.
     *
     * @param manager the manager to associate with
     * @throws IllegalStateException if this skill has already been associated with another manager
     */
    public void setManager(SkillManager manager) {
        Preconditions.checkState(this.manager == null || this.manager == manager, "cannot reassociate skill!");
        this.manager = manager;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * @return the cost, in books, of this skill
     */
    public int getTalentPointCost() {
        return bookCost;
    }

    /**
     * Sets the amount of books that need to be paid in order to acquire this skill.
     *
     * @param bookCost the new book cost
     */
    public void setBookCost(int bookCost) {
        this.bookCost = bookCost;
    }

    /**
     * @return the item stack used to represent this skill in inventories
     */
    public ItemStack getIconStack() {
        return iconStack;
    }

    /**
     * Sets the item stack used to represent this skill in inventories.
     *
     * @param iconStack the new icon stack
     */
    public void setIconStack(ItemStack iconStack) {
        this.iconStack = iconStack.clone();
    }

    /**
     * @return this skill's {@link #getIconStack() icon stack}, or a default stack
     */
    public ItemStack getDisplayStack() {
        return iconStack == null ? createDefaultStack() : getIconStack();
    }

    private ItemStack createDefaultStack() {
        return new ItemStack(Material.PAPER);
    }

    /**
     * Gets the handler specification of this skill. The spec is an arbitrary string interpreted by
     * the handler manager that then assigns the appropriate handler, which configures itself
     * according to the spec. If the spec is null, this skill does nothing.
     *
     * @return the current handler spec of this skill
     */
    public String getHandlerSpec() {
        return handlerSpec;
    }

    /**
     * Sets the {@link #getHandlerSpec() handler specification} of this skill.
     *
     * @param handlerSpec the new handler spec
     */
    public void setHandlerSpec(String handlerSpec) {
        this.handlerSpec = handlerSpec;
    }

    /**
     * Checks if given model skill has the same id as this skill.
     *
     * @param modelSkill the model skill to compare to
     * @return whether the ids are the same
     */
    public boolean matches(ObtainedSkill modelSkill) {
        Preconditions.checkNotNull(modelSkill, "modelSkill");
        return modelSkill.matches(this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID_PATH, id);
        map.put(BOOK_COST_PATH, bookCost);
        map.put(ICON_PATH, iconStack);
        map.put(HANDLER_SPEC_PATH, handlerSpec);
        return map;
    }

    /**
     * Deserialises an instance of this class previously serialized using {@link #serialize()}.
     *
     * @param source the map to deserialise
     * @return a skill corresponding to the map
     */
    public static Skill deserialize(Map<String, Object> source) {
        Preconditions.checkArgument(source.containsKey("id"), "source must have id");
        String id = (String) source.get(ID_PATH);
        Skill skill = new Skill(id);
        skill.setHandlerSpec((String) source.get(HANDLER_SPEC_PATH));
        if (source.containsKey(BOOK_COST_PATH)) {
            skill.setBookCost((Integer) source.get(BOOK_COST_PATH));
        }
        if (source.containsKey(ICON_PATH)) {
            Object iconObj = source.get(ICON_PATH);
            if (iconObj != null) {
                Preconditions.checkArgument(iconObj instanceof ItemStack,
                        "icon must be an item stack, is: %s", String.valueOf(iconObj));
                skill.setIconStack((ItemStack) iconObj);
            }
        }
        return skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return id.equals(skill.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id='" + id + '\'' +
                ", bookCost=" + bookCost +
                ", handlerSpec='" + handlerSpec + '\'' +
                '}';
    }
}
