/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n.exception;

import li.l1t.common.exception.InternalException;
import li.l1t.common.intake.i18n.Message;

/**
 * A non-sensitive system-induced exception with a localisable message.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class I18nInternalException extends InternalException implements InternationalException {
    private static final Object[] NO_PARAMS = new Object[0];
    private final Object[] params;

    public I18nInternalException(String messageKey) {
        super(messageKey);
        this.params = NO_PARAMS;
    }

    public I18nInternalException(String messageKey, Object... params) {
        super(messageKey);
        this.params = params;
    }

    public I18nInternalException(String messageKey, Throwable cause) {
        super(messageKey, cause);
        this.params = NO_PARAMS;
    }

    @Override
    public String getWrapperMessageKey() {
        return "core!format.error-internal";
    }

    @Override
    public String getMessageKey() {
        return getMessage();
    }

    @Override
    public Object[] getMessageParameters() {
        return params;
    }

    @Override
    public Message toMessage() {
        return Message.of(getWrapperMessageKey(), Message.of(getMessageKey(), getMessageParameters()));
    }
}
