/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.yaml;

import me.minotopia.expvp.Nameable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Manages YAML-backed identifiable objects in-memory and delegates loading.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public interface YamlManager<T extends Nameable> {
    /**
     * Loads all objects from disk.
     */
    void loadAll();

    /**
     * @return the directory where objects are stored
     */
    File getDirectory();

    /**
     * @return an immutable view of the current in-memory collection of objects
     */
    Collection<T> getAll();

    /**
     * Gets am object by its id, if it exists in memory. Otherwise returns null.
     *
     * @param objId the id of the object to fetch
     * @return an object with given id (case-sensitive) or null
     */
    T get(String objId);

    /**
     * Checks whether an object exists in memory by a given id.
     *
     * @param objId the id of the object to seek
     * @return whether anm object exists by the given id
     */
    boolean contains(String objId);

    /**
     * Creates a new object and saves it to its file.
     *
     * @param objId the unique id of the new tree
     * @return the created tree
     * @throws IOException if an error occurs writing the file
     */
    T create(String objId) throws IOException;

    /**
     * Saves an object to its file.
     *
     * @param obj the object to save
     * @throws IOException if an error occurs writing to the file
     */
    void save(T obj) throws IOException;
}
