/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.model.friend;

import li.l1t.common.exception.DatabaseException;
import me.minotopia.expvp.api.friend.Friendship;
import me.minotopia.expvp.api.model.PlayerData;

import java.util.Optional;

/**
 * Provides friendship instances from the backend.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
public interface FriendshipRepository {
    /**
     * Finds the single friendship containing given player.
     *
     * @param playerData the player to find
     * @return an optional containing the found friendship, or an empty optional if there is no such friendship
     * @throws DatabaseException if a database error occurs or a database inconsistency is detected
     */
    Optional<Friendship> findFriendshipWith(PlayerData playerData) throws DatabaseException;

    /**
     * @param friendship the friendship to delete
     * @throws IllegalArgumentException if given friendship wasn't fetched from this repository
     */
    void delete(Friendship friendship);

    Friendship create(PlayerData source, PlayerData target);
}
