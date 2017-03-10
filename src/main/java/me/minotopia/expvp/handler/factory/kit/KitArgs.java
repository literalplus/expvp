/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory.kit;

import li.l1t.common.string.Args;

/**
 * Represents the list of comma-separated arguments passed to kit handler factories.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public class KitArgs extends Args {
    public KitArgs(String argString) {
        super(argString, ",");
    }

    public KitArgs(String... args) {
        super(args);
    }
}
