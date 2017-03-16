/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory;

import me.minotopia.expvp.api.handler.factory.HandlerFactory;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;

/**
 * Saves typing of type arguments, basically.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-10
 */
public class TestHandlerFactory extends MapCompoundHandlerFactory<HandlerFactory> {
    public TestHandlerFactory(HandlerSpecNode parent, String ownHandlerSpec) {
        super(parent, ownHandlerSpec);
    }
}
