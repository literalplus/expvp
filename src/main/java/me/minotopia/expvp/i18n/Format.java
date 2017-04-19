/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;


import li.l1t.common.i18n.Message;

/**
 * Static utility class that contains a few helpful methods for wrapping messages in common formats.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-29
 */
public class Format {
    private static Message wrapIn(String key, Message message) {
        return Message.of("core!format." + key, message);
    }

    public static Message broadcast(Message message) {
        return wrapIn("broadcast", message);
    }

    public static Message broadcast(String key, Object... arguments) {
        return wrapIn("broadcast", Message.of(key, arguments));
    }

    public static Message internalError(Message message) {
        return wrapIn("error-internal", message);
    }

    public static Message internalError(String key, Object... arguments) {
        return wrapIn("error-internal", Message.of(key, arguments));
    }

    public static Message userError(Message message) {
        return wrapIn("error-user", message);
    }

    public static Message userError(String key, Object... arguments) {
        return wrapIn("error-user", Message.of(key, arguments));
    }

    public static Message warning(Message message) {
        return wrapIn("warning", message);
    }

    public static Message warning(String key, Object... arguments) {
        return wrapIn("warning", Message.of(key, arguments));
    }

    public static Message result(Message message) {
        return wrapIn("result", message);
    }

    public static Message result(String key, Object... arguments) {
        return wrapIn("result", Message.of(key, arguments));
    }

    public static Message success(Message message) {
        return wrapIn("result-success", message);
    }

    public static Message success(String key, Object... arguments) {
        return wrapIn("result-success", Message.of(key, arguments));
    }

    public static Message listHeader(Message message) {
        return wrapIn("list-header", message);
    }

    public static Message listHeader(String key, Object... arguments) {
        return wrapIn("list-header", Message.of(key, arguments));
    }

    public static Message header(Message message) {
        return wrapIn("header", message);
    }

    public static Message header(String key, Object... arguments) {
        return wrapIn("header", Message.of(key, arguments));
    }

    public static Message listItem(Message message) {
        return wrapIn("list-item", message);
    }

    public static Message listItem(String key, Object... arguments) {
        return wrapIn("list-item", Message.of(key, arguments));
    }

    public static Message bool(boolean input) {
        return Message.of(input ? "core!yes" : "core!no");
    }
}
