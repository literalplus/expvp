/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.handler.HandlerFactoryGraph;
import me.minotopia.expvp.api.handler.factory.CompoundHandlerFactory;
import me.minotopia.expvp.api.handler.factory.HandlerFactory;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;
import me.minotopia.expvp.api.handler.factory.InvalidHandlerSpecException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for handler spec nodes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
abstract class MultiHandlerSpecNode extends AbstractHandlerSpecNode implements CompoundHandlerFactory {
    private final Map<String, HandlerFactory> children = new HashMap<>();

    MultiHandlerSpecNode(HandlerSpecNode parent, String ownHandlerSpec) {
        super(parent, ownHandlerSpec);
    }

    protected HandlerFactory findChildOrFail(String handlerSpec) throws InvalidHandlerSpecException {
        String firstNodeId = findFirstNodeId(handlerSpec);
        HandlerFactory child = children.get(firstNodeId);
        if (child == null) {
            throw invalidSpecException("Unknown sub-spec", handlerSpec);
        }
        return child;
    }

    private String findFirstNodeId(String handlerSpec) {
        String[] parts = splitIntoNodeIds(handlerSpec);
        return parts[0];
    }

    private String[] splitIntoNodeIds(String handlerSpec) {
        return handlerSpec.split(HandlerFactoryGraph.SEPARATOR);
    }

    @Override
    public void addChild(HandlerFactory child) {
        Preconditions.checkNotNull(child, "child");
        children.put(child.getHandlerSpec(), child);
    }

    @Override
    public Collection<HandlerFactory> getChildren() {
        return children.values();
    }
}
