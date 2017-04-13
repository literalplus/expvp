/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.exception;

import me.minotopia.expvp.i18n.exception.I18nUserException;

/**
 * Thrown if an unknown player was requested.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-13
 */
public class UnknownPlayerException extends I18nUserException {
    public UnknownPlayerException(String searchCriteria) {
        super("error!player.unknown", searchCriteria);
    }
}
