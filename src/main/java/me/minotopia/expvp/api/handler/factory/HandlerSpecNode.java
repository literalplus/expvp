/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.factory;

import me.minotopia.expvp.api.handler.HandlerGraph;

/**
 * Represents a node in the handler spec graph. Handler specs tell {@link HandlerFactory}s
 * what kind of handler to create. The complete handler spec represents a path in the {@link HandlerGraph}.
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
     * @return the immediate parent node of this node, or null if this is the root node
     */
    HandlerSpecNode getParent();
}
