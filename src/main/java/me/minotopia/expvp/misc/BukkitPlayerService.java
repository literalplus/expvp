/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
