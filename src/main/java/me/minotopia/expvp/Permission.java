/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;

import org.bukkit.permissions.Permissible;

/**
 * The enumeration of permissions used by ExPvP to limit who can do what.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-15
 */
public enum Permission {
    /**
     * Allows to perform basic administrative tasks, like view skills and trees.
     */
    ADMIN_BASIC("expvp.admin.basic"),
    /**
     * Allows to create and edit skills.
     */
    ADMIN_SKILL("expvp.admin.skill"),
    /**
     * Allows to create and edit skill trees.
     */
    ADMIN_TREE("expvp.admin.tree"),
    /**
     * Allows to override certain safety measures and delete content such as skills and skill
     * trees.
     */
    ADMIN_OVERRIDE("expvp.admin.override"),
    /**
     * Allows to administer player data.
     */
    ADMIN_PLAYERS("expvp.admin.players");

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public boolean has(Permissible permissible) {
        return permissible.hasPermission(value());
    }

    @Override
    public String toString() {
        return value();
    }
}
