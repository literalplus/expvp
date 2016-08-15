/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;

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
    ADMIN_BASE("expvp.admin.basic"),
    /**
     * Allows to create and edit skills.
     */
    ADMIN_SKILL("expvp.admin.skill"),
    /**
     * Allows to create and edit skill trees.
     */
    ADMIN_TREE("expvp.admin.tree"),
    /**
     * Allows to override certain safety measures and delete content such as skills and skill trees.
     */
    ADMIN_OVERRIDE("expvp.admin.override")
    ;

    private final String bukkitPermission;

    Permission(String bukkitPermission) {
        this.bukkitPermission = bukkitPermission;
    }

    public String value() {
        return bukkitPermission;
    }
}
