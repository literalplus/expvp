/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;

/**
 * Abstract base class for handler spec nodes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public abstract class AbstractHandlerSpecNode implements HandlerSpecNode {
    private final String ownHandlerSpec;

    protected AbstractHandlerSpecNode(String ownHandlerSpec) {
        this.ownHandlerSpec = Preconditions.checkNotNull(ownHandlerSpec, "ownHandlerSpec");
    }

    @Override
    public String getHandlerSpec() {
        return ownHandlerSpec;
    }
}
