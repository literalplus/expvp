/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill;

import org.bukkit.inventory.ItemStack;

/**
 * Represents the metadata of an obtainable skill. Note that skills are pluggable, so they do not
 * keep a reference to the skill tree they're part of - they can also be part of multiple trees.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
public class Skill {
    private final String id;
    private int bookCost;
    private String name;
    private ItemStack iconStack;
    private String handlerSpec; //arbitrary string handled by handlers - like XYC Kit API

    public Skill(String id) {
        this.id = id;
    }

    //TODO
}
