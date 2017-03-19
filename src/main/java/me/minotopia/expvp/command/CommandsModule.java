/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.util.CommandHelper;
import me.minotopia.expvp.command.permission.EnumPermissionInvokeListener;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.plugin.Plugin;

/**
 * Provides the dependency wiring for the commands module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-09
 */
public class CommandsModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Singleton
    @Provides
    CommandsManager commandsManager(Plugin plugin) {
        CommandsManager commandsManager = new CommandsManager(plugin);
        commandsManager.setLocale(sender -> I18n.getLocaleFor(CommandHelper.getSenderId(sender)));
        commandsManager.getBuilder().addInvokeListener(new EnumPermissionInvokeListener());
        return commandsManager;
    }
}
