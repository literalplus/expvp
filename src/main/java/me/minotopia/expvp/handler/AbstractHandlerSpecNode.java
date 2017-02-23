/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.handler.HandlerGraph;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

/**
 * Abstract base class for handler spec nodes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public abstract class AbstractHandlerSpecNode implements HandlerSpecNode {
    private final HandlerSpecNode parent;
    private final String ownHandlerSpec;

    protected AbstractHandlerSpecNode(HandlerSpecNode parent, String ownHandlerSpec) {
        this.parent = parent;
        this.ownHandlerSpec = Preconditions.checkNotNull(ownHandlerSpec, "ownHandlerSpec");
    }

    @Override
    public String getHandlerSpec() {
        return ownHandlerSpec;
    }

    @Override
    public HandlerSpecNode getParent() {
        return parent;
    }

    public String getFullHandlerSpec() {
        Deque<String> parentSpecs = new ArrayDeque<>();
        HandlerSpecNode current = this;
        do {
            parentSpecs.addFirst(current.getHandlerSpec());
        } while ((current = current.getParent()) != null);
        return parentSpecs.stream()
                .collect(Collectors.joining(HandlerGraph.SEPARATOR));
    }
}
