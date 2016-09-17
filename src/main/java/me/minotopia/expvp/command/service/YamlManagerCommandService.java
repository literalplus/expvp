/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.service;

import li.l1t.common.intake.exception.InternalException;
import li.l1t.common.intake.exception.UserException;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.Nameable;
import me.minotopia.expvp.yaml.YamlManager;
import org.bukkit.command.CommandSender;

import java.io.IOException;

/**
 * Provides base utilities for working with object registries managed by {@link YamlManager}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-13
 */
public class YamlManagerCommandService<T extends Nameable> extends CommandService {
    protected final YamlManager<T> manager;
    private String objectTypeName;

    YamlManagerCommandService(EPPlugin plugin, YamlManager<T> manager, String objectTypeName) {
        super(plugin);
        this.manager = manager;
        this.objectTypeName = objectTypeName;
    }

    public void saveObject(T object) {
        try {
            manager.save(object);
        } catch (IOException e) {
            throw new InternalException(String.format(
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
            throw new UserException(String.format(
                    "Es gibt bereits einen " + objectTypeName + " mit der ID '%s'!",
                    id));
        }
    }

    public T getObjectOrFail(String id) {
        T object = manager.get(id);
        if (object == null) {
            throw new UserException(String.format(
                    "Es gibt keinen %s mit der ID '%s'!",
                    objectTypeName, id));
        }
        return object;
    }

    public void changeName(T object, String newName, CommandSender sender) {
        String previousName = object.getName();
        object.setName(newName);
        saveObject(object);
        sendChangeNotification("Name", previousName, newName, object, sender);
    }

    public void sendChangeNotification(String description, Object previous, Object changed,
                                       T object, CommandSender sender) {
        sender.sendMessage(String.format(
                "§e➩ %s des %ss '%s' von '%s' auf '%s§e' geändert.",
                description, objectTypeName, object.getId(), previous, changed));
    }

    public YamlManager<T> getManager() {
        return manager;
    }
}
