/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;


import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.xyplugin.GenericXyPlugin;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.api.service.SkillObtainmentService;
import me.minotopia.expvp.command.CommandSkillAdmin;
import me.minotopia.expvp.command.CommandSkillTreeAdmin;
import me.minotopia.expvp.command.CommandSkills;
import me.minotopia.expvp.command.permission.EnumPermissionInvokeListener;
import me.minotopia.expvp.command.service.SkillCommandService;
import me.minotopia.expvp.command.service.SkillTreeCommandService;
import me.minotopia.expvp.kits.KitHandler;
import me.minotopia.expvp.logging.LoggingManager;
import me.minotopia.expvp.player.HibernatePlayerDataService;
import me.minotopia.expvp.skill.meta.SkillManager;
import me.minotopia.expvp.skill.obtainment.CostCheckingObtainmentService;
import me.minotopia.expvp.skill.obtainment.SimpleSkillObtainmentService;
import me.minotopia.expvp.skill.tree.SkillTreeManager;
import me.minotopia.expvp.util.SessionProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.UUIDCharType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Plugin class for interfacing with the Bukkit API.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-20
 */
public class EPPlugin extends GenericXyPlugin {
    private final String chatPrefix = "§6[§7ExP§6] ";
    private KitHandler kitHandler;
    private Logger log;
    private SessionProvider sessionProvider;
    private SkillTreeManager skillTreeManager;
    private SkillManager skillManager;
    private CommandsManager commandsManager;
    private PlayerDataService playerDataService;
    private SkillObtainmentService obtainmentService;

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

            //Load kits
            this.kitHandler = new KitHandler(this);

            //Initialise Hibernate ORM
            SessionFactory sessionFactory = initHibernate(getClassLoader());
            this.sessionProvider = new SessionProvider(sessionFactory);

            //Instantiate services
            playerDataService = new HibernatePlayerDataService(sessionProvider);
            obtainmentService = new CostCheckingObtainmentService(
                    new SimpleSkillObtainmentService(playerDataService),
                    playerDataService, sessionProvider
            );

            //Load skill trees and skills
            skillManager = new SkillManager(new File(getDataFolder(), "skills"), obtainmentService);
            skillTreeManager = new SkillTreeManager(new File(getDataFolder(), "skilltrees"), skillManager);

            // Register commands
            registerCommands();

            saveConfig();
        } catch (Exception e) {
            //Using jul here because Log4J2 might not have been initialised
            getLogger().log(java.util.logging.Level.SEVERE, " --- Exception while trying to enable Expvp: ", e);
            getServer().getConsoleSender().sendMessage("§4 --- Unable to enable Expvp ^^^^ ---");
            getLogger().log(java.util.logging.Level.SEVERE, "*** Enabling whitelist...");
            getServer().setWhitelist(true);
            if (!getServer().getOnlinePlayers().isEmpty()) {
                getServer().getOnlinePlayers().stream()
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
        }
    }

    private void registerCommands() {
        commandsManager = new CommandsManager(this);
        commandsManager.getTranslator().setLocale(Locale.GERMAN);
        commandsManager.getBuilder().addInvokeListener(new EnumPermissionInvokeListener());
        registerInjections();
        commandsManager.registerCommand(new CommandSkillAdmin(), "ska", "skilladmin");
        commandsManager.registerCommand(new CommandSkillTreeAdmin(), "sta", "treeadmin");
        commandsManager.registerCommand(new CommandSkills(), "sk", "skills");
    }

    private void registerInjections() {
        new SkillCommandService(skillManager).registerInjections(commandsManager);
        new SkillTreeCommandService(skillTreeManager).registerInjections(commandsManager);
    }

    SessionFactory initHibernate(ClassLoader classLoader) throws IOException { //TODO: Querydsl
        File propertiesFile = new File(getDataFolder(), "hibernate.properties");
        if (!propertiesFile.exists()) {
            Files.copy(getResource("hibernate.properties"), propertiesFile.toPath());
        }

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .loadProperties(propertiesFile) // configures settings from hibernate.properties
                .configure(getClass().getResource("/hibernate.cfg.xml"))
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
            StandardServiceRegistryBuilder.destroy(registry);
            getLogger().log(java.util.logging.Level.SEVERE, "Could not build Hibernate SessionFactory: ", e);
            getLogger().log(java.util.logging.Level.SEVERE, "*** This is a CRITICAL error");
            throw new RuntimeException("Unable to connect to database. This is a critical error.", e);
        }
    }

    @Override
    public void disable() {
        try {
            sessionProvider.getSessionFactory().close();
        } catch (Exception e) {
            //Using jul here because Log4J2 might not work
            getLogger().log(java.util.logging.Level.SEVERE, "Exception while trying to disable " +
                    "Expvp: ", e);
            getServer().getConsoleSender().sendMessage("§4 ---- Unable to disable Expvp ^^^^ ----");
        }
    }

    public void printBanner(CommandSender receiver) {
        receiver.sendMessage(new String[]{
                "§6~~~~§7███████╗██╗§6~~§7██╗██████╗§6~§7██╗§6~~~§7██╗██████╗§6~",
                "§6~~~~§7██╔════╝╚██╗██╔╝██╔══██╗██║§6~~~§7██║██╔══██╗",
                "§6~~~~§7█████╗§6~~~§7╚███╔╝§6~§7██████╔╝██║§6~~~§7██║██████╔╝",
                "§6~~~~§7██╔══╝§6~~~§7██╔██╗§6~§7██╔═══╝§6~§7╚██╗§6~§7██╔╝██╔═══╝§6~",
                "§6~~~~§7███████╗██╔╝~██╗██║§6~~~~~~§7╚████╔╝§6~§7██║§6~~~~~",
                "§6~~~~§7╚══════╝╚═╝§6~~§7╚═╝╚═╝§6~~~~~~~§7╚═══╝§6~~§7╚═╝§6~~~~~",
                "§6~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~",
                "§6Expvp Minecraft Game Mode for MinoTopia.me",
                getPluginVersion().toString(),
                "§6Copyright (C) 2016 Philipp Nowak (Literallie)",
                "§6See the LICENSE.txt file in the plugin jar for more info."
        }); // it says "Expvp"
    }

    @Override
    public String getChatPrefix() {
        return chatPrefix;
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

    public CommandsManager getCommandsManager() {
        return commandsManager;
    }

    public PlayerDataService getPlayerDataService() {
        return playerDataService;
    }

    public SkillObtainmentService getSkillObtainmentService() {
        return obtainmentService;
    }

    public SkillTreeManager getSkillTreeManager() {
        return skillTreeManager;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }
}
