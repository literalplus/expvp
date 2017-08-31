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

package me.minotopia.expvp.misc;

import com.google.inject.Inject;
import me.minotopia.expvp.api.misc.PlayerService;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides player instances via the Bukkit API.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
public class BukkitPlayerService implements PlayerService {
    private final Server server;

    @Inject
    public BukkitPlayerService(Server server) {
        this.server = server;
    }

    @Override
    public Optional<Player> findOnlinePlayer(UUID playerId) {
        return Optional.ofNullable(server.getPlayer(playerId));
    }

    @Override
    public Optional<Player> findOnlinePlayer(String name) {
        return Optional.ofNullable(server.getPlayerExact(name));
    }

    @Override
    public Optional<OfflinePlayer> findOfflinePlayer(UUID playerId) {
        return Optional.ofNullable(server.getOfflinePlayer(playerId));
    }

    @Override
    public String findNameFor(UUID playerId, CommandSender receiver) {
        return findOfflinePlayer(playerId)
                .map(OfflinePlayer::getName)
                .orElseGet(() -> I18n.loc(receiver, "core!unknown"));
    }
}
