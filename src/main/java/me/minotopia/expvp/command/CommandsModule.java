/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.google.inject.*;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.CommandExceptionListener;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.util.CommandHelper;
import me.minotopia.expvp.command.permission.EnumPermissionInvokeListener;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.InternationalException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

/**
 * Provides the dependency wiring for the commands module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-09
 */
public class CommandsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CommandEPAdmin.class);
        bind(CommandSkillAdmin.class);
        bind(CommandSkills.class);
        bind(CommandSkillTreeAdmin.class);
        bind(CommandSpawnAdmin.class);
        bind(CommandMapVote.class);
        bind(CommandSpawnLink.class);
    }

    @Singleton
    @Provides
    CommandsManager commandsManager(Plugin plugin, Injector injector) {
        CommandsManager commandsManager = new CommandsManager(plugin);
        commandsManager.setLocale(sender -> I18n.getLocaleFor(CommandHelper.getSenderId(sender)));
        commandsManager.getBuilder().addInvokeListener(new EnumPermissionInvokeListener());
        injector.getAllBindings().values().forEach(binding -> bindGuiceToIntake(commandsManager, binding));
        commandsManager.addExceptionListener(i18nCommandExceptionListener());
        commandsManager.getHelpProvider().setMetaTranslator((key, locale) -> I18n.loc(locale, key));
        return commandsManager;
    }

    @SuppressWarnings("unchecked")
    private <T> void bindGuiceToIntake(CommandsManager commandsManager, Binding<T> binding) {
        commandsManager.bind((Class<T>) binding.getKey().getTypeLiteral().getRawType())
                .toProvider(guiceToIntakeProvider(binding));
    }

    private <T> Provider<T> guiceToIntakeProvider(Binding<T> binding) {
        return new Provider<T>() {
            @Override
            public boolean isProvided() {
                return true;
            }

            @Override
            public T get(CommandArgs commandArgs, List<? extends Annotation> list) throws ArgumentException, ProvisionException {
                return binding.getProvider().get();
            }

            @Override
            public List<String> getSuggestions(String s) {
                return Collections.emptyList();
            }
        };
    }

    private CommandExceptionListener i18nCommandExceptionListener() {
        return (argLine, sender, exception) -> {
            if (exception instanceof InternationalException) {
                sendI18nExceptionText(sender, (InternationalException) exception);
                return false;
            } else if (exception.getCause() instanceof InternationalException) {
                //cannot change message to translated version, so this looks like the only way
                //we can still benefit from Intake-Spigot's translation
                sendI18nExceptionText(sender, (InternationalException) exception.getCause());
                return false;
            }
            return true;
        };
    }

    private void sendI18nExceptionText(CommandSender sender, InternationalException exception) {
        sender.sendMessage(I18n.loc(sender, exception.toMessage()));
    }
}
