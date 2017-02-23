/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import me.minotopia.expvp.api.handler.HandlerGraph;
import me.minotopia.expvp.api.handler.event.ListenerHandlerMap;
import me.minotopia.expvp.api.handler.kit.KitHandlerMap;

/**
 * Represents the root node of the Expvp handler graph.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-02-21
 */
public class EPHandlerGraph implements HandlerGraph {

    public EPHandlerGraph() {

    }


    @Override
    public KitHandlerMap kits() {
        throw new UnsupportedOperationException(); //TODO
    }

    @Override
    public ListenerHandlerMap listeners() {
        throw new UnsupportedOperationException(); //TODO
    }
}
