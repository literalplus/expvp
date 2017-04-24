/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.friend.exception;

import me.minotopia.expvp.i18n.exception.I18nUserException;

/**
 * Thrown if an action cannot be completed because a player already has a friend.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-24
 */
public class ExistingFriendshipException extends I18nUserException {
    public ExistingFriendshipException() {
        super("error!friend.existing-fs");
    }
}
