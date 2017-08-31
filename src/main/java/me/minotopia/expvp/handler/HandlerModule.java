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

package me.minotopia.expvp.handler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.handler.HandlerFactoryGraph;
import me.minotopia.expvp.api.handler.HandlerMap;
import me.minotopia.expvp.api.handler.HandlerService;
import me.minotopia.expvp.api.handler.kit.KitService;
import me.minotopia.expvp.api.handler.kit.compilation.KitBaseline;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompiler;
import me.minotopia.expvp.handler.damage.DamageHandlerCaller;
import me.minotopia.expvp.handler.factory.EPHandlerFactoryGraph;
import me.minotopia.expvp.handler.factory.HandlerFactoryWiring;
import me.minotopia.expvp.handler.kit.SkillKitService;
import me.minotopia.expvp.handler.kit.compilation.SkillKitCompiler;
import me.minotopia.expvp.handler.kit.compilation.StaticKitBaseline;

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
        bind(KitBaseline.class).to(StaticKitBaseline.class);
        bind(KitCompiler.class).to(SkillKitCompiler.class);
        bind(KitService.class).to(SkillKitService.class);
        bind(DamageHandlerCaller.class);
        bind(HandlerFactoryWiring.class);
    }

    @Singleton
    @Provides
    HandlerFactoryGraph handlerFactoryGraph(HandlerFactoryWiring wiring) {
        EPHandlerFactoryGraph graph = new EPHandlerFactoryGraph();
        wiring.wire(graph);
        return graph;
    }
}
