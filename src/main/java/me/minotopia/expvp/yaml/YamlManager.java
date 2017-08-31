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

package me.minotopia.expvp.yaml;

import me.minotopia.expvp.api.Identifiable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Manages YAML-backed identifiable objects in-memory and delegates loading.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
public interface YamlManager<T extends Identifiable> {
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

    /**
     * Attempts to remove an object from this manager and the underlying data store.
     *
     * @param obj the object to remove
     * @throws IOException           if an error occurs deleting the object from the underlying data
     *                               store. Note that the object will not be removed from this
     *                               manager if such error occurs.
     * @throws IllegalStateException if the object does not exist in this manager. Note that if this
     *                               error occurs, the underlying data store will have the object
     *                               removed.
     */
    void remove(T obj) throws IOException;
}
