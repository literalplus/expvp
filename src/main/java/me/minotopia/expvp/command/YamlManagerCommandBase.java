/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import li.l1t.common.intake.i18n.Message;
import me.minotopia.expvp.api.Identifiable;
import me.minotopia.expvp.command.service.YamlManagerCommandService;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.command.CommandSender;

import java.io.IOException;

/**
 * Base class for commands related to YamlManager.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-13
 */
abstract class YamlManagerCommandBase<T extends Identifiable, S extends YamlManagerCommandService<T>>
        extends AbstractServiceBackedCommand<S> {
    protected YamlManagerCommandBase(S commandService) {
        super(commandService);
    }

    public abstract String getObjectTypeName();

    T createNew(CommandSender sender,
                String id) throws IOException {
        T object = service().createObjectWithExistsCheck(id);
        I18n.sendLoc(sender, Message.of("admin!reg.created", service().getObjectType(), id));
        return object;
    }
}
