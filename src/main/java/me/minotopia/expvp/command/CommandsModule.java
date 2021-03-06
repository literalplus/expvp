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

package me.minotopia.expvp.command;

import com.google.inject.*;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.CommandExceptionListener;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.util.CommandHelper;
import me.minotopia.expvp.command.chat.CommandChatClear;
import me.minotopia.expvp.command.chat.CommandGlobalMute;
import me.minotopia.expvp.command.chat.CommandMessage;
import me.minotopia.expvp.command.chat.CommandReply;
import me.minotopia.expvp.command.permission.EnumPermissionInvokeListener;
import me.minotopia.expvp.command.provider.LocalDateProvider;
import me.minotopia.expvp.command.provider.LocalTimeProvider;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.InternationalException;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * Provides the dependency wiring for the commands module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-09
 */
public class CommandsModule extends AbstractModule {
    private final Logger LOGGER = LoggingManager.getLogger(CommandsModule.class);

    @Override
    protected void configure() {
        bind(CommandEPAdmin.class);
        bind(CommandSkillAdmin.class);
        //bind(CommandSkills.class); // -> menu
        bind(CommandSpawn.class);
        bind(CommandSkillTreeAdmin.class);
        bind(CommandSpawnAdmin.class);
        bind(CommandMapVote.class);
        bind(CommandSpawnLink.class);
        bind(CommandEP.class);
        bind(CommandFriend.class);
        bind(CommandLang.class);
        bind(CommandStats.class);
        bind(CommandMessage.class);
        bind(CommandReply.class);
        bind(CommandChatClear.class);
        bind(CommandGlobalMute.class);
        bind(CommandExTimes.class);
    }

    @Singleton
    @Provides
    CommandsManager commandsManager(Plugin plugin, Injector injector) {
        CommandsManager commandsManager = new CommandsManager(plugin);
        commandsManager.setLocale(sender -> I18n.getLocaleFor(CommandHelper.getSenderId(sender)));
        commandsManager.getBuilder().addInvokeListener(new EnumPermissionInvokeListener());
        injector.getAllBindings().values().forEach(binding -> bindGuiceToIntake(commandsManager, binding));
        commandsManager.addExceptionListener(i18nCommandExceptionListener());
        commandsManager.getHelpProvider().setMetaTranslator(
                (key, locale) -> key == null || !key.contains("!") ? key : I18n.loc(locale, key)
        );
        commandsManager.bind(LocalDate.class).toProvider(new LocalDateProvider());
        commandsManager.bind(LocalTime.class).toProvider(new LocalTimeProvider());
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
                sendI18nExceptionText(sender, argLine, (InternationalException) exception);
                return false;
            } else if (exception.getCause() instanceof InternationalException) {
                //cannot change message to translated version, so this looks like the only way
                //we can still benefit from Intake-Spigot's translation
                sendI18nExceptionText(sender, argLine, (InternationalException) exception.getCause());
                return false;
            }
            return true;
        };
    }

    private void sendI18nExceptionText(CommandSender sender, String argLine, InternationalException exception) {
        sender.sendMessage(I18n.loc(sender, exception.toMessage()));
        if (exception.needsLogging()) {
            LOGGER.warn("Exception exceuting command /" + argLine + ":", exception);
        }
    }
}
