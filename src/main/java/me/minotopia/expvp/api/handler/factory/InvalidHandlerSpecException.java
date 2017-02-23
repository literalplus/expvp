/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.factory;


import li.l1t.common.exception.UserException;

/**
 * Thrown if a {@link HandlerFactory} cannot parse a handler spec.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public class InvalidHandlerSpecException extends UserException {
    private final String handlerSpec;
    private final HandlerFactory factory;

    public InvalidHandlerSpecException(String message, String handlerSpec, HandlerFactory factory) {
        super(message);
        this.handlerSpec = handlerSpec;
        this.factory = factory;
    }

    public String getHandlerSpec() {
        return handlerSpec;
    }

    public HandlerFactory getFactory() {
        return factory;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " for spec " + getHandlerSpec() + " from factory " + factory;
    }
}
