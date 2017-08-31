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
        sendChangeNotification("admin!spawn.prop.loc", LocationHelper.prettyPrint(previousLocation),
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
