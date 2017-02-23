/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory;

import me.minotopia.expvp.api.handler.HandlerFactoryGraph;

/**
 * Represents the root node of the Expvp handler factory graph.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-02-21
 */
public class EPHandlerFactoryGraph extends MapCompoundHandlerFactory implements HandlerFactoryGraph {
    public EPHandlerFactoryGraph() {
        super(null, "");
    }
}
