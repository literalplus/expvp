/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketListener;
import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.xyplugin.GenericXyPlugin;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.command.AutoRegister;
import me.minotopia.expvp.command.CommandsModule;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.logging.LoggingManager;
import me.minotopia.expvp.util.SessionProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.UUIDCharType;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Plugin class for interfacing with the Bukkit API.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-20
 */
public class EPPlugin extends GenericXyPlugin {
    private Logger log;
    private SessionProvider sessionProvider;
    private Injector injector;

    public EPPlugin() {

    }

    EPPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void reloadConfig() {
        log(Level.INFO, "Reloading Expvp config...");
        super.reloadConfig();
        log(Level.DEBUG, "Reloaded Expvp config!");
    }

    @Override
    public void enable() {
        printBanner(getServer().getConsoleSender());

        try {
            LoggingManager.setPlugin(this);
            log = LoggingManager.getLogger(getClass());
            log.info("===== Hello, friend");
            log.info("Am " + getPluginVersion().toString());

            // Set locale data folder
            I18n.setDataFolder(new File(getDataFolder(), "lang"));

            // Initialise Hibernate ORM
            this.sessionProvider = new SessionProvider(initHibernate(getClassLoader()), this);

            // Initialise Dependency Injection
            injector = Guice.createInjector(new EPRootModule(this), new CommandsModule());

            registerBoundListeners();
            registerBoundCommands();

            saveConfig();

            PlayerInitService initService = inject(PlayerInitService.class);
            getServer().getOnlinePlayers()
                    .forEach(initService::callInitHandlers);
        } catch (Exception e) {
            handleEnableException(e);
        }
    }

    public <T> T inject(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    private void registerBoundListeners() {
        doForAllBound(Listener.class, listener -> getServer().getPluginManager().registerEvents(listener, this));
        doForAllBound(PacketListener.class, listener -> ProtocolLibrary.getProtocolManager().addPacketListener(listener));
    }

    private <T> void doForAllBound(Class<T> baseType, Consumer<T> consumer) {
        injector.getBindings().entrySet().stream()
                .filter(bindingEntryInstanceOf(baseType))
                .map(this::bindingEntryToInstance)
                .map(baseType::cast)
                .forEach(consumer);
    }

    private <T> Predicate<Map.Entry<Key<?>, ?>> bindingEntryInstanceOf(Class<T> baseType) {
        return en -> baseType.isAssignableFrom(en.getKey().getTypeLiteral().getRawType());
    }

    private Object bindingEntryToInstance(Map.Entry<?, Binding<?>> entry) {
        return entry.getValue().getProvider().get();
    }

    private void registerBoundCommands() {
        CommandsManager commandsManager = inject(CommandsManager.class);
        injector.getBindings().entrySet().stream()
                .filter(bindingEntryHasAnnotation(AutoRegister.class))
                .map(this::bindingEntryToInstance)
                .forEach(handler -> {
                    AutoRegister meta = handler.getClass().getAnnotation(AutoRegister.class);
                    commandsManager.registerCommand(handler, meta.value(), meta.aliases());
                });
    }

    private Predicate<Map.Entry<Key<?>, Binding<?>>> bindingEntryHasAnnotation(Class<? extends Annotation> annotationClass) {
        return en -> en.getKey().getTypeLiteral().getRawType().isAnnotationPresent(annotationClass);
    }

    private void handleEnableException(Exception e) {
        //Using jul here because Log4J2 might not have been initialised
        getLogger().log(java.util.logging.Level.SEVERE, " --- Exception while trying to enable Expvp: ", e);
        getServer().getConsoleSender().sendMessage("§4 --- Unable to enable Expvp ^^^^ ---");
        getLogger().log(java.util.logging.Level.SEVERE, "*** Enabling whitelist...");
        getServer().setWhitelist(true);
        getServer().getOnlinePlayers()
                .forEach(player -> {
                    if (player.isOp() || Permission.ADMIN_BASIC.has(player)) {
                        player.sendMessage("§4§l *** Unable to enable Expvp");
                        player.sendMessage("§4§l *** This is a critical error");
                        player.sendMessage("§4§l *** Non-authorised personnel will be exterminated");
                        player.sendMessage("§4§l *** Don't panic, call Ghostbusters.");
                        player.sendMessage("§4§l *** Whitelist has been enabled.");
                        player.sendMessage("§c Technical description: " + e.getClass().getName() + ": " + e.getLocalizedMessage());
                    } else {
                        player.sendMessage("§4§l *** Es gab einen internen Fehler.");
                        player.sendMessage("§4§l *** Zu deiner eigenen Sicherheit wirst du gekickt.");
                        player.kickPlayer("§cInterner Fehler bei Expvp.\n§aKeine Panik.\n§eBitte kontaktiere das Team.");
                    }
                });
    }

    SessionFactory initHibernate(ClassLoader classLoader) throws IOException { //TODO: Querydsl
        File propertiesFile = new File(getDataFolder(), "hibernate.properties");
        if (!propertiesFile.exists()) {
            Files.copy(getResource("hibernate.properties"), propertiesFile.toPath());
        }
        File xmlFile = new File(getDataFolder(), "hibernate.cfg.tmp.xml");
        Files.copy(getResource("hibernate.cfg.xml"), xmlFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .loadProperties(propertiesFile) // configures settings from hibernate.properties
                .configure(xmlFile)
                .build();
        try {
            List<Class<?>> classes = EntityScanner //Hibernate doesn't do entity scanning sadly
                    .scanPackages(
                            Collections.singletonList(classLoader),
                            EPPlugin.class.getPackage().getName()
                    ).result();
            MetadataSources sources = new MetadataSources(registry);
            classes.forEach(sources::addAnnotatedClass);
            return sources
                    .getMetadataBuilder()
                    //Map UUIDs to CHAR(36) instead of binary; readability > storage
                    .applyBasicType(UUIDCharType.INSTANCE, UUID.class.getName())
                    .build()
                    .buildSessionFactory();
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().equals("MALFORMED")) {
                getLogger().severe(" *** Sadly, Hibernate/Fluent Hibernate cannot deal with reloads due to certain limitations. Please restart the server instead.");
            }
            StandardServiceRegistryBuilder.destroy(registry);
            getLogger().log(java.util.logging.Level.SEVERE, "Could not build Hibernate SessionFactory: ", e);
            getLogger().log(java.util.logging.Level.SEVERE, "*** This is a CRITICAL error");
            throw new RuntimeException("Unable to connect to database. This is a critical error.", e);
        }
    }

    @Override
    public void disable() {
        try {
            PlayerInitService initService = inject(PlayerInitService.class);
            getServer().getOnlinePlayers()
                    .forEach(initService::callDeInitHandlers);

            if (sessionProvider != null) {
                sessionProvider.getSessionFactory().close();
            }
        } catch (Exception e) {
            //Using jul here because Log4J2 might not work
            getLogger().log(java.util.logging.Level.SEVERE, "Exception while trying to disable " +
                    "Expvp: ", e);
            getServer().getConsoleSender().sendMessage("§4 ---- Unable to disable Expvp ^^^^ ----");
        }
    }

    public void printBanner(CommandSender receiver) {
        receiver.sendMessage(new String[]{
                "§78888888888§6~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~",
                "§7888§6~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~",
                "§7888§6~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~",
                "§78888888§6~~~~§7888§6~~§7888§6~§788888b.§6~~§7888§6~~§7888§6~§788888b.§6~~",
                "§7888§6~~~~~~~~§7`Y8bd8P'§6~§7888§6~§7\"88b§6~§7888§6~~§7888§6~§7888§6~§7\"88b§6~",
                "§7888§6~~~~~~~~~~§7X88K§6~~~§7888§6~~§7888§6~§7Y88§6~~§788P§6~§7888§6~~§7888§6~",
                "§7888§6~~~~~~~~§7.d8\"\"8b.§6~§7888§6~§7d88P§6~~§7Y8bd8P§6~~§7888§6~§7d88P§6~",
                "§78888888888§6~§7888~~§7888§6~§788888P\"§6~~~~§7Y88P§6~~~§788888P\"§6~~",
                "§6~~~~~~~~~~~~~~~~~~~~§7888§6~~~~~~~~~~~~~~~§7888§6~~~~~~",
                "§6~~~~~~~~~~~~~~~~~~~~§7888§6~~~~~~~~~~~~~~~§7888§6~~~~~~",
                "§6~~~~~~~~~~~~~~~~~~~~§7888§6~~~~~~~~~~~~~~~§7888§6~~~~~~",
                "§6Expvp Minecraft Game Mode for MinoTopia.me",
                getPluginVersion().toString(),
                "§6Copyright (C) 2016-2017 Philipp Nowak (Literallie)",
                "§6See the LICENSE.txt file in the plugin jar for more info."
        }); // it says "Expvp"
    }

    @Override
    public String getChatPrefix() {
        return "§6[§7Exp§6] ";
    }


    /**
     * @return the SessionProvider used by this plugin
     */
    public SessionProvider getSessionProvider() {
        return sessionProvider;
    }

    private void log(Level level, String message) {
        if (log != null) {
            log.log(level, message);
        }
    }
}
