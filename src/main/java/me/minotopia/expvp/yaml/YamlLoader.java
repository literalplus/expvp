/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
 * @author <a href="http://xxyy.github.io/">xxyy</a>
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
