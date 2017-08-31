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

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import li.l1t.common.intake.exception.UncheckedException;
import me.minotopia.expvp.api.Identifiable;
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
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
public abstract class AbstractYamlLoader<T extends Identifiable> implements YamlLoader<T> {
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
            Files.copy(file, newFile);
            file.delete();
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
