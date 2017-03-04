/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.event;

import me.minotopia.expvp.api.handler.HandlerMap;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;

/**
 * A handler map that stores {@link ListenerHandler}s.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-02-22
 */
public interface ListenerHandlerMap extends HandlerMap<ListenerHandler>, HandlerSpecNode {
}