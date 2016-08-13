/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.service;

import li.l1t.common.intake.exception.CommandArgumentException;
import me.minotopia.expvp.Identifiable;
import me.minotopia.expvp.yaml.YamlManager;

import java.io.IOException;

/**
 * Provides base utilities for working with object registries managed by {@link YamlManager}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-13
 */
public class YamlManagerCommandService<T extends Identifiable> {
    protected final YamlManager<T> manager;
    private String objectTypeName;

    YamlManagerCommandService(YamlManager<T> manager, String objectTypeName) {
        this.manager = manager;
        this.objectTypeName = objectTypeName;
    }

    public void saveObject(T object) {
        try {
            manager.save(object);
        } catch (IOException e) {
            throw new IllegalStateException(String.format(
                    "Fehler beim Speichern vom %s mit der ID '%s'",
                    objectTypeName, object.getId()), e);
        }
    }

    public T createObjectWithExistsCheck(String id) throws IOException {
        assureThereIsNoObjectWithId(id);
        return manager.create(id);
    }

    public void assureThereIsNoObjectWithId(String id) {
        if (manager.contains(id)) {
            throw new CommandArgumentException(String.format(
                    "Es gibt bereits einen " + objectTypeName + " mit der ID '%s'!",
                    id));
        }
    }

    public T getObjectOrFail(String id) {
        T object = manager.get(id);
        if (object == null) {
            throw new CommandArgumentException(String.format(
                    "Es gibt keinen %s mit der ID '%s'!",
                    objectTypeName, id));
        }
        return object;
    }

    public YamlManager<T> getManager() {
        return manager;
    }
}
