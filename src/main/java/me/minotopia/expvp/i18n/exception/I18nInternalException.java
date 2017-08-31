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

package me.minotopia.expvp.i18n.exception;

import li.l1t.common.exception.InternalException;
import li.l1t.common.i18n.Message;

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
