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

package me.minotopia.expvp.handler.factory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.handler.HandlerFactoryGraph;
import me.minotopia.expvp.api.handler.factory.HandlerFactory;
import me.minotopia.expvp.handler.factory.damage.CompoundDamageHandlerFactory;
import me.minotopia.expvp.handler.factory.kit.CompoundKitHandlerFactory;

/**
 * Represents the root node of the Expvp handler factory graph.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-02-21
 */
@Singleton
public class EPHandlerFactoryGraph extends MapCompoundHandlerFactory<HandlerFactory> implements HandlerFactoryGraph {
    private CompoundKitHandlerFactory rootKitFactory;
    private CompoundDamageHandlerFactory rootDamageFactory;

    @Inject
    public EPHandlerFactoryGraph() {
        super(null, "");
        rootKitFactory = new CompoundKitHandlerFactory(this, "kit");
        addChild(rootKitFactory);
        rootDamageFactory = new CompoundDamageHandlerFactory("dmg");
        addChild(rootDamageFactory);
    }

    @Override
    public CompoundKitHandlerFactory kits() {
        return rootKitFactory;
    }

    @Override
    public CompoundDamageHandlerFactory damages() {
        return rootDamageFactory;
    }
}
