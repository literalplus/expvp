/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;


import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import me.minotopia.expvp.kits.KitHandler;
import me.minotopia.expvp.logging.LoggingManager;
import me.minotopia.expvp.model.BaseEntity;
import me.minotopia.expvp.skill.tree.SkillTreeManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.CommandSender;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.UUIDCharType;

import io.github.xxyy.common.games.kits.factory.CommandKitFactory;
import io.github.xxyy.common.xyplugin.GenericXyPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
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
    private SessionFactory sessionFactory;
    private SkillTreeManager skillTreeManager;

    @Override
    public void reloadConfig() {
        log(Level.INFO, "Reloading ExPvP config...");
        super.reloadConfig();
        log(Level.DEBUG, "Reloaded ExPvP config!");
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
            initHibernate();

            //Load skill trees
            skillTreeManager = new SkillTreeManager(new File(getDataFolder(), "skilltrees"));

            // Register commands
            setExecAndCompleter(new CommandKitFactory("exp.admin.kits", "ExPvP Kit management",
                    kitHandler, kitHandler.getObjectiveDescriptions()), "kitfabrik");

            saveConfig();
        } catch (Exception e) {
            //Using jul here because Log4J2 might not have been initialised
            getLogger().log(java.util.logging.Level.SEVERE, " --- Exception while trying to enable ExPvP: ", e);
            getServer().getConsoleSender().sendMessage("§4 --- Unable to enable ExPvP ^^^^ ---");
        }
    }

    private void initHibernate() throws IOException { //TODO: Querydsl
        File propertiesFile = new File(getDataFolder(), "hibernate.properties");
        if (!propertiesFile.exists()) {
            Files.copy(getResource("hibernate.properties"), propertiesFile.toPath());
        }

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .loadProperties(propertiesFile)
                .configure() // configures settings from hibernate.properties
                .build();
        try {
            List<Class<?>> classes = EntityScanner //Hibernate doesn't do entity scanning, so we need
                    .scanPackages(BaseEntity.class.getPackage().getName()).result(); //to do it for them
            MetadataSources sources = new MetadataSources(registry);
            classes.forEach(sources::addAnnotatedClass);
            sources.getMetadataBuilder().applyBasicType(        //Map UUIDs to CHAR(36) instead of
                    UUIDCharType.INSTANCE, UUID.class.getName() //binary; readability > storage
            );
            sessionFactory = sources
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            getLogger().log(java.util.logging.Level.SEVERE, "Could not build Hibernate SessionFactory: ", e);
            throw e;
        }
    }

    @Override
    public void disable() {
        try {

        } catch (Exception e) {
            //Using jul here because Log4J2 might not work
            getLogger().log(java.util.logging.Level.SEVERE, "Exception while trying to disable ExPvP: ", e);
            getServer().getConsoleSender().sendMessage("§4 ---- Unable to disable ExPvP ^^^^ ----");
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
                "§6ExPvP Minecraft Game Mode for MinoTopia.me",
                getPluginVersion().toString(),
                "§6Copyright (C) 2016 Philipp Nowak (Literallie)",
                "§6See the LICENSE.txt file in the plugin jar for more info."
        }); // it says "ExPvP"
    }

    @Override
    public String getChatPrefix() {
        return chatPrefix;
    }


    /**
     * @return the Hibernate SessionFactory for this plugin
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private void log(Level level, String message) {
        if (log != null) {
            log.log(level, message);
        }
    }
}
