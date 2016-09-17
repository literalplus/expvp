/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.yaml;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import li.l1t.common.intake.exception.UncheckedException;
import me.minotopia.expvp.api.Nameable;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
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
                tryMoveAwayBecauseUnreadable(file);
            }
        }
        return obj;
    }

    private void tryMoveAwayBecauseUnreadable(File file) {
        try {
            File newFile = new File(manager.getDirectory(), file.getName() + ".broken_bkp");
            Files.move(file, newFile);
            LOGGER.warn("Moved to {} to prevent data loss", newFile.getAbsolutePath());
        } catch (IOException e1) {
            throw UncheckedException.wrap(e1); //something is seriously wrong
        }
    }

    @Override
    public void delete(String objId) throws IOException {
        File file = getFile(objId);
        if (file.exists()) {
            java.nio.file.Files.delete(file.toPath());
        }
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
    protected void checkDoesNotExistInManager(String objId) {
        Preconditions.checkArgument(!manager.contains(objId),
                "an object already exists with that id: %s", objId);
    }

    /**
     * Checks to make sure that given id does already have a mapping.
     *
     * @param objId the id to check
     * @throws IllegalArgumentException if no object exists with that id
     */
    protected void checkExistsInManager(String objId) {
        Preconditions.checkArgument(manager.contains(objId),
                "no object exists with that id: %s", objId);
    }

    /**
     * @return a file filter filtering for object files by file extension
     */
    protected FileFilter getFileFilter() {
        return f -> f.getName().endsWith(getFileExtension());
    }
}
