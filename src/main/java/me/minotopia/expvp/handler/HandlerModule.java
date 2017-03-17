/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.handler.HandlerFactoryGraph;
import me.minotopia.expvp.api.handler.HandlerMap;
import me.minotopia.expvp.api.handler.HandlerService;
import me.minotopia.expvp.handler.factory.EPHandlerFactoryGraph;
import me.minotopia.expvp.handler.factory.HandlerFactoryWiring;

/**
 * Provides the dependency wiring for the skill handler module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public class HandlerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HandlerMap.class).to(SimpleHandlerMap.class);
        bind(HandlerService.class).to(SimpleHandlerService.class);
    }

    @Singleton
    @Provides
    HandlerFactoryGraph handlerFactoryGraph(HandlerFactoryWiring wiring) {
        EPHandlerFactoryGraph graph = new EPHandlerFactoryGraph();
        wiring.wire(graph);
        return graph;
    }
}
