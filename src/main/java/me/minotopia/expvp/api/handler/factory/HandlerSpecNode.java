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

package me.minotopia.expvp.api.handler.factory;

import me.minotopia.expvp.api.handler.HandlerFactoryGraph;

/**
 * Represents a node in the handler spec graph. Handler specs tell {@link HandlerFactory}s
 * what kind of handler to create. The complete handler spec represents a path in the {@link HandlerFactoryGraph}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public interface HandlerSpecNode {
    /**
     * @return the immediate {@link HandlerSpecNode handler spec} of this node, without any prefixes of its parents and
     * without any suffixes for its children - specifically does not contain the level separator used by the graph the
     * node is part of
     */
    String getHandlerSpec();

    /**
     * @param parent the immediate parent node of this node, or null if this is the root node
     */
    void setParent(HandlerSpecNode parent);

    /**
     * @return the immediate parent node of this node, or null if this is the root node
     */
    HandlerSpecNode getParent();
}
