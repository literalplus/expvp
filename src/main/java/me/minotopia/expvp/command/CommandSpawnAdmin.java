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

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Validate;
import li.l1t.common.chat.ComponentSender;
import li.l1t.common.chat.XyComponentBuilder;
import li.l1t.common.i18n.Message;
import li.l1t.common.intake.provider.annotation.Colored;
import li.l1t.common.intake.provider.annotation.Merged;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.command.permission.EnumRequires;
import me.minotopia.expvp.command.service.SpawnCommandService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import me.minotopia.expvp.spawn.SpawnManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Collection;

/**
 * A command that provides tools for creation, deletion and modification of map spawns.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-12
 */
@AutoRegister("spa")
public class CommandSpawnAdmin extends YamlManagerCommandBase<MapSpawn, SpawnCommandService> {
    private final DisplayNameService names;
    private final SpawnService spawns;

    @Inject
    protected CommandSpawnAdmin(SpawnCommandService commandService, DisplayNameService names, SpawnService spawns) {
        super(commandService);
        this.names = names;
        this.spawns = spawns;
    }

    @Override
    public String getObjectTypeName() {
        return "Skill";
    }

    @Command(aliases = "show", desc = "", usage = "")
    public void show(CommandSender sender) {
        I18n.sendLoc(sender, names.displayName(spawns.getCurrentSpawn().orElse(null)));
    }

    @Command(aliases = "new", min = 1,
            desc = "Erstellt einen neuen Spawn",
            help = "Erstellt einen neuen Spawn\nzur Verwendung als Map.\nDie Id besteht " +
                    "dabei aus\nZahlen, Buchstaben und Bindestrichen\nund ist eindeutig.",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_SPAWN)
    public void newSkill(@Sender Player player, @Validate(regex = "[a-zA-Z0-9\\-]+") String id)
            throws IOException {
        MapSpawn newSpawn = createNew(player, id);
        service().changeLocation(newSpawn, player);
    }

    @Command(aliases = "set", min = 1,
            desc = "Ändert die Position",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_SPAWN)
    public void editLocation(@Sender Player player, MapSpawn spawn)
            throws IOException {
        service().changeLocation(spawn, player);
    }

    @Command(aliases = "author", min = 1,
            desc = "Ändert den Autor",
            usage = "[id] [Autor...]")
    @EnumRequires(Permission.ADMIN_SPAWN)
    public void editAuthor(CommandSender sender, MapSpawn spawn, @Colored @Merged String author)
            throws IOException {
        service().changeAuthor(spawn, author, sender);
    }

    @Command(aliases = "info", min = 1,
            desc = "Zeigt Infos zu einem Spawn",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_BASIC)
    public void spawnInfo(CommandSender sender, MapSpawn spawn) {
        I18n.sendLoc(sender, Format.header(Message.of("admin!spawn.info.header", spawn.getId())));
        I18n.sendLoc(sender, Format.result(Message.of("admin!spawn.info.name", names.displayName(spawn))));
        I18n.sendLoc(sender, Format.result(Message.of("admin!spawn.info.author", spawn.getAuthor())));
        ComponentSender.sendTo(
                new XyComponentBuilder("➩ ", ChatColor.YELLOW)
                        .append(I18n.loc(sender, Message.of("admin!spawn.info.location", spawn.getLocation().prettyPrint())))
                        .append(" ")
                        .appendIf(spawn.hasLocation(), I18n.loc(sender, "admin!spawn.info.teleport-button"))
                        .hintedCommand(spawn.getLocation().toTpCommand(sender.getName())),
                sender
        );
    }

    @Command(aliases = "list",
            desc = "Zeigt alle Spawns")
    @EnumRequires(Permission.ADMIN_SPAWN)
    public void list(EPPlugin plugin, SpawnManager manager, CommandSender sender) {
        Collection<MapSpawn> spawns = manager.getAll();
        if (spawns.isEmpty()) {
            throw new I18nUserException("admin!spawn.list.none");
        }
        spawns.forEach(spawn -> ComponentSender.sendTo(
                new XyComponentBuilder("-➩ ", ChatColor.YELLOW)
                        .append(spawn.getId())
                        .hintedCommand("/spa info " + spawn.getId()),
                sender
        ));
    }

    @Command(aliases = "remove", min = 1,
            desc = "Löscht einen Spawn",
            usage = "[id]")
    @EnumRequires({Permission.ADMIN_SPAWN, Permission.ADMIN_OVERRIDE})
    public void removeSkill(CommandSender sender, MapSpawn spawn)
            throws IOException {
        service().getManager().remove(spawn);
    }
}
