/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.service;

import com.google.inject.Inject;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.misc.XyLocation;
import li.l1t.common.util.LocationHelper;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.command.provider.YamlObjectProvider;
import me.minotopia.expvp.spawn.SpawnManager;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Provides commonly used utilities for commands working with spawns.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-12
 */
public class SpawnCommandService extends YamlManagerCommandService<MapSpawn> {
    @Inject
    public SpawnCommandService(SpawnManager manager, PlayerDataService players,
                               SessionProvider sessionProvider, CommandsManager commandsManager) {
        super(sessionProvider, players, manager, "admin!spawn.spawn", commandsManager);
        registerInjections(commandsManager);
    }

    @Override
    protected void registerInjections(CommandsManager commandsManager) {
        commandsManager.bind(SpawnCommandService.class).toInstance(this);
        commandsManager.bind(MapSpawn.class).toProvider(new YamlObjectProvider<>(this));
    }

    public void changeLocation(MapSpawn spawn, Player player) {
        XyLocation previousLocation = spawn.getLocation();
        spawn.setLocation(XyLocation.of(player.getLocation()));
        saveObject(spawn);
        sendChangeNotification("admin!spawn.prop.loc", previousLocation.prettyPrint(),
                LocationHelper.prettyPrint(player.getLocation()), spawn, player);
    }

    public void changeAuthor(MapSpawn spawn, String newAuthor, CommandSender sender) {
        String previousAuthor = spawn.getAuthor();
        spawn.setAuthor(newAuthor);
        saveObject(spawn);
        sendChangeNotification("admin!spawn.prop.author", previousAuthor, newAuthor, spawn, sender);
    }

    @Override
    public SpawnManager getManager() {
        return (SpawnManager) super.getManager();
    }
}
