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

import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Manages loading of identifiable objects serialised as individual YAML files in a common
 * directory.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
public interface YamlLoader<T> {
    /**
     * Loads all available objects from the object directory. If an error occurs loading, it is
     * logged and then ignored.
     *
     * @return all successfully loaded objects
     */
    Set<T> loadFromDirectory();

    /**
     * Saves an object's metadata to the file specified by the object id.
     *
     * @param obj the object to save
     * @throws IOException if an error occurs writing the file
     */
    void saveToFile(T obj) throws IOException;

    /**
     * Loads an object from a file.
     *
     * @param file the file to load from
     * @return the loaded object
     * @throws IOException                   if an error occurs reading the file or the file does
     *                                       not exist
     * @throws InvalidConfigurationException if the file is not a valid YAML file
     */
    T loadFromFile(File file) throws IOException, InvalidConfigurationException;

    /**
     * @return the file extension the objects are saved with
     */
    String getFileExtension();

    /**
     * Creates a new object by an id and saves it to its file.
     *
     * @param objId the id to create an instance with
     * @return the created object
     * @throws IOException if an error occurs writing the file
     */
    T create(String objId) throws IOException;

    /**
     * Deletes the file backing an object. Does nothing if the file does not exist. Does not modify
     * the state of the manager.
     * @param objId the id of the object
     * @throws IOException if an error occurs deleting the file
     */
    void delete(String objId) throws IOException;

    /**
     * Gets the id of the contained object from a file name.
     *
     * @param file the file
     * @return the id of the object stored in the file
     */
    String getObjectId(File file);

    /**
     * Gets the file corresponding to the id of an object.
     *
     * @param objId the object id
     * @return the file
     */
    File getFile(String objId);
}
