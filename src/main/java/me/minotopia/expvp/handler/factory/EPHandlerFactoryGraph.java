/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory;

import com.google.inject.Inject;
import me.minotopia.expvp.api.handler.HandlerFactoryGraph;
import me.minotopia.expvp.api.handler.factory.HandlerFactory;
import me.minotopia.expvp.handler.factory.kit.CompoundKitHandlerFactory;

/**
 * Represents the root node of the Expvp handler factory graph.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-02-21
 */
public class EPHandlerFactoryGraph extends MapCompoundHandlerFactory<HandlerFactory>
        implements HandlerFactoryGraph {
    private CompoundKitHandlerFactory rootKitFactory;

    @Inject
    public EPHandlerFactoryGraph(HandlerFactoryWiring wiring) {
        super(null, "");
        rootKitFactory = new CompoundKitHandlerFactory(this, "kit");
        addChild(rootKitFactory);
        wiring.wire(this);
    }

    @Override
    public CompoundKitHandlerFactory kits() {
        return rootKitFactory;
    }
}
