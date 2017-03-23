/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
