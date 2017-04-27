/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.friend;

import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Provides access to friend data.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
public interface FriendService {
    Optional<PlayerData> findFriend(Player player);

    Optional<PlayerData> findFriend(PlayerData data);

    void removeFriend(Player player);
}
