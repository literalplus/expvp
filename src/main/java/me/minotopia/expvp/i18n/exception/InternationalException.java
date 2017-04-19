/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n.exception;

import li.l1t.common.i18n.Message;

/**
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public interface InternationalException {
    String getMessageKey();

    Object[] getMessageParameters();

    String getWrapperMessageKey();

    Message toMessage();
}
