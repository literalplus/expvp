/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.meta;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the metadata of an obtainable skill. Note that skills are pluggable, so they do not
 * keep a reference to the skill tree they're part of - they can also be part of multiple trees.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
public class Skill implements ConfigurationSerializable {
    public static final String ID_PATH = "id";
    public static final String BOOK_COST_PATH = "cost";
    public static final String NAME_PATH = "name";
    public static final String ICON_PATH = "icon";
    public static final String HANDLER_SPEC_PATH = "handler";
    private final String id;
    private int bookCost;
    private String name;
    private ItemStack iconStack;
    private String handlerSpec; //arbitrary string handled by handlers - like XYC Kit API

    /**
     * Creates a new skill.
     * @param id the unique id for the new skill
     */
    public Skill(String id) {
        this.id = id;
    }

    /**
     * @return a non-null display name for this skill
     */
    public String getDisplayName() {
        return name == null ? id : name;
    }

    /**
     * @return the unique id identifying this skill
     */
    public String getId() {
        return id;
    }

    /**
     * @return the cost, in books, of this skill
     */
    public int getBookCost() {
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
     * @return the non-unique, human-readable name of this skill
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the non-unique, human-readable name of this skill.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
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
        this.iconStack = iconStack;
    }

    /**
     * Gets the handler specification of this skill. The spec is an arbitrary string interpreted
     * by the handler manager that then assigns the appropriate handler, which configures itself
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

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID_PATH, id);
        map.put(BOOK_COST_PATH, bookCost);
        map.put(NAME_PATH, name);
        map.put(ICON_PATH, iconStack);
        map.put(HANDLER_SPEC_PATH, handlerSpec);
        return map;
    }

    /**
     * Deserialises an instance of this class previously serialized using {@link #serialize()}.
     * @param source the map to deserialise
     * @return a skill corresponding to the map
     */
    public static Skill deserialize(Map<String, Object> source) {
        Preconditions.checkArgument(source.containsKey("id"), "source must have id");
        String id = (String) source.get(ID_PATH);
        Skill skill = new Skill(id);
        skill.setName((String) source.get(NAME_PATH));
        skill.setHandlerSpec((String) source.get(HANDLER_SPEC_PATH));
        if (source.containsKey(BOOK_COST_PATH)) {
            skill.setBookCost((Integer) source.get(BOOK_COST_PATH));
        }
        if(source.containsKey(ICON_PATH)) {
            Object iconObj = source.get(ICON_PATH);
            Preconditions.checkArgument(iconObj instanceof ItemStack,
                    "icon must be an item stack, is: %s", String.valueOf(iconObj));
            skill.setIconStack((ItemStack) iconObj);
        }
        return skill;
    }
}
