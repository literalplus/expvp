/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.factory;

/**
 * Represents a node in the handler spec graph. Handlers specs tell {@link SkillHandlerFactory}s
 * what kind of handler to create given a handler spec.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-14
 */
public interface HandlerSpecNode {
    /**
     * @return the immediate {@link HandlerSpecNode handler spec} of this node, without any prefixes
     * of its parents and without any suffixes for its children - specifically does not contain the
     * level separator used by the graph the node is part of
     */
    String getHandlerSpec();
}
