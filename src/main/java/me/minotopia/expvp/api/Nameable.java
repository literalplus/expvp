/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api;

/**
 * Something that can be uniquely identified and optionally assigned a name.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-13
 */
public interface Nameable extends Identifiable {
    String getName();

    void setName(String newName);

    default String getDisplayName() {
        if (getName() == null) {
            return getId();
        } else {
            return getName();
        }
    }
}
