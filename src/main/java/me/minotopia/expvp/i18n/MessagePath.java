/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

/**
 * Represents the unique path to a localised message with bundle and message key.
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
        if (parts.length != 2 || parts[1].isEmpty()) {
            throw new IllegalArgumentException(String.format("Malformed message path: '%s'", path));
        }
        if (parts[0].isEmpty()) {
            return new MessagePath("core", parts[1]);
        } else {
            return new MessagePath(parts[0], parts[1]);
        }
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
