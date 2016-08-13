/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import me.minotopia.expvp.Nameable;
import me.minotopia.expvp.command.service.YamlManagerCommandService;
import org.bukkit.command.CommandSender;

import java.io.IOException;

/**
 * Base class for commands related to YamlManager.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-13
 */
abstract class YamlManagerCommandBase<T extends Nameable> {
    public abstract String getObjectTypeName();

    void createNew(YamlManagerCommandService<T> service, CommandSender sender,
                   String id, String name) throws IOException {
        T object = service.createObjectWithExistsCheck(id);
        object.setName(name);
        service.saveObject(object);
        sender.sendMessage(String.format(
                "§e➩ Neuer %s mit der ID '%s' und dem Namen '%s' erstellt.",
                getObjectTypeName(), id, name));
    }
}
