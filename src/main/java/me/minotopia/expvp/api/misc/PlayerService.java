/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.misc;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * Retrieves player objects and related data from UUIDs.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
public interface PlayerService {
    Optional<Player> findOnlinePlayer(UUID playerId);

    Optional<Player> findOnlinePlayer(String name);

    Optional<OfflinePlayer> findOfflinePlayer(UUID playerId);

    String findNameFor(UUID playerId, CommandSender receiver);
}
