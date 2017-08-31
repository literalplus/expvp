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

package me.minotopia.expvp.api.handler;

import me.minotopia.expvp.api.handler.factory.CompoundHandlerFactory;
import me.minotopia.expvp.api.handler.factory.HandlerFactory;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;
import me.minotopia.expvp.handler.factory.damage.CompoundDamageHandlerFactory;
import me.minotopia.expvp.handler.factory.kit.CompoundKitHandlerFactory;

/**
 * Represents the root node of a complete handler factory graph. The handler spec of the root node is an empty
 * string, meaning that all handler specs start with the separator.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-02-22
 */
public interface HandlerFactoryGraph extends CompoundHandlerFactory<HandlerFactory> {
    /**
     * The separator that separates the nodes in handler specs.
     */
    String SEPARATOR = "/";

    /**
     * {@inheritDoc}
     *
     * @return an empty string for the root node of the handler graph
     */
    @Override
    default String getHandlerSpec() {
        return "";
    }

    /**
     * {@inheritDoc}
     *
     * @return null for the root node of the handler graph
     */
    @Override
    default HandlerSpecNode getParent() {
        return null;
    }

    CompoundKitHandlerFactory kits();

    CompoundDamageHandlerFactory damages();
}
