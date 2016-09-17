/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api;

/**
 * Something that can be identified with a String id.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-13
 */
public interface Identifiable {
    /**
     * @return the unique id identifying this object
     */
    String getId();
}
