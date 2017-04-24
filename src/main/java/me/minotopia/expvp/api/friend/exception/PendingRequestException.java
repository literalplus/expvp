/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.friend.exception;

import me.minotopia.expvp.api.friend.FriendRequest;
import me.minotopia.expvp.i18n.exception.I18nUserException;

/**
 * Thrown if a player already has a pending friend request.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
public class PendingRequestException extends I18nUserException {
    private final FriendRequest pending;

    public PendingRequestException(FriendRequest pending) {
        super("error!friend.pending-request");
        this.pending = pending;
    }

    public FriendRequest getPending() {
        return pending;
    }
}
