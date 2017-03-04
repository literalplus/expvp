/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.command.service.CommandService;

/**
 * Abstract base class for commands backed by a service.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public class AbstractServiceBackedCommand<T extends CommandService> implements ServiceBackedCommand<T> {
    private T commandService;

    @Override
    public void setCommandService(T commandService) {
        this.commandService = commandService;
    }

    public T service() {
        Preconditions.checkNotNull(commandService, "commandService");
        return commandService;
    }
}
