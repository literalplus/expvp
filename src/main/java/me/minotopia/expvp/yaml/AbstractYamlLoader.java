/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.yaml;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.Nameable;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

/**
 * Generic implementation of a class that loads objects from a directory of YAML files.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public abstract class AbstractYamlLoader<T extends Nameable> implements YamlLoader<T> {
    public static final String DATA_PATH = "data";
    private static Logger LOGGER = LoggingManager.getLogger(AbstractYamlLoader.class);
    private final YamlManager<T> manager;

    protected AbstractYamlLoader(YamlManager<T> manager) {
        this.manager = manager;
    }

    @Override
    public Set<T> loadFromDirectory() {
        Set<T> obj = new HashSet<>();
        if (!manager.getDirectory().isDirectory() && !manager.getDirectory().mkdirs()) {
            throw new IllegalStateException("couldn't make parent dirs help me");
        }
        for (File file : manager.getDirectory().listFiles(getFileFilter())) {
            try {
                obj.add(loadFromFile(file));
            } catch (Exception e) { //Make sure we can still load the other objects at least
                LOGGER.warn("Failed to load object from " + file.getAbsolutePath() + ": ", e);
                e.printStackTrace();
            }
        }
        return obj;
    }

    @Override
    public String getObjectId(File file) {
        return file.getName().replace(getFileExtension(), "");
    }

    @Override
    public File getFile(String objId) {
        return new File(manager.getDirectory(), objId + getFileExtension());
    }

    /**
     * Checks to make sure that given id does not already have a mapping.
     *
     * @param objId the id to check for uniqueness
     * @throws IllegalArgumentException if another object already exists with that id
     */
    protected void checkExists(String objId) {
        Preconditions.checkArgument(!manager.contains(objId),
                "an object already exists with that id: %s", objId);
    }

    /**
     * @return a file filter filtering for object files by file extension
     */
    protected FileFilter getFileFilter() {
        return f -> f.getName().endsWith(getFileExtension());
    }
}
