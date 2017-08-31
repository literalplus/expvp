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

package me.minotopia.expvp.i18n;

/**
 * Represents the unique path to a localised message with bundle and message key.
 * <p>String representations look like {@code core!some.key}, where {@code core} is the resource bundle name, and
 * {@code some.key} is the message key in that bundle.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-03
 */
class MessagePath {
    private final String bundle;
    private final String key;

    private MessagePath(String bundle, String key) {
        this.bundle = bundle;
        this.key = key;
    }

    public static MessagePath of(String path) {
        String[] parts = path.split("!");
        if (parts.length != 2 || parts[1].isEmpty() || parts[0].isEmpty()) {
            throw new IllegalArgumentException(String.format("Malformed message path: '%s'", path));
        }
        return new MessagePath(parts[0], parts[1]);
    }

    public String bundle() {
        return bundle;
    }

    public String key() {
        return key;
    }

    @Override
    public String toString() {
        return "message '" + key + "' in '" + bundle + "'";
    }
}
