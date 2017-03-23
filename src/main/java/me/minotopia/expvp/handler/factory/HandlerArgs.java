/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory;

import li.l1t.common.string.Args;

/**
 * Represents the list of comma-separated arguments passed to handler factories.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public class HandlerArgs extends Args {
    public HandlerArgs(String argString) {
        super(argString, ",");
    }

    public HandlerArgs(String... args) {
        super(args);
    }
}
