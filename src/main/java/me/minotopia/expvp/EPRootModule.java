/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.inject.AbstractModule;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.inject.DataFolder;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.handler.HandlerModule;
import me.minotopia.expvp.i18n.EPDisplayNameService;
import me.minotopia.expvp.misc.EPPlayerInitService;
import me.minotopia.expvp.model.ModelModule;
import me.minotopia.expvp.respawn.RespawnModule;
import me.minotopia.expvp.score.ScoreModule;
import me.minotopia.expvp.skill.SkillModule;
import me.minotopia.expvp.skilltree.SkillTreeModule;
import me.minotopia.expvp.spawn.SpawnModule;
import me.minotopia.expvp.ui.InventoryMenuModule;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;

/**
 * Provides the dependency wiring for common base functionality common to multiple modules of Expvp.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-09
 */
public class EPRootModule extends AbstractModule {
    private final EPPlugin plugin;

    public EPRootModule(EPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(Plugin.class).toInstance(plugin);
        bind(EPPlugin.class).toInstance(plugin);
        bind(Server.class).toInstance(plugin.getServer());
        bind(BukkitScheduler.class).toInstance(plugin.getServer().getScheduler());
        bind(File.class).annotatedWith(DataFolder.class).toInstance(plugin.getDataFolder());
        bind(SessionProvider.class).toInstance(plugin.getSessionProvider());
        bind(DisplayNameService.class).to(EPDisplayNameService.class);
        bind(PlayerInitService.class).to(EPPlayerInitService.class);
        bind(ProtocolManager.class).toProvider(ProtocolLibrary::getProtocolManager);
        bind(TaskService.class).toInstance(plugin.tasks());
        install(new ModelModule());
        install(new SkillModule());
        install(new SkillTreeModule());
        install(new HandlerModule());
        install(new ScoreModule());
        install(new InventoryMenuModule());
        install(new RespawnModule());
        install(new SpawnModule());
    }
}
