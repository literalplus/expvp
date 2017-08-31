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

package me.minotopia.expvp.handler.factory;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.handler.HandlerFactoryGraph;
import me.minotopia.expvp.api.handler.SkillHandler;
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
abstract class MultiHandlerSpecNode<T extends HandlerFactory, R extends SkillHandler>
        extends AbstractHandlerSpecNode implements CompoundHandlerFactory<T> {
    private final Map<String, T> children = new HashMap<>();

    MultiHandlerSpecNode(HandlerSpecNode parent, String ownHandlerSpec) {
        super(parent, ownHandlerSpec);
    }

    MultiHandlerSpecNode(String ownHandlerSpec) {
        this(null, ownHandlerSpec);
    }

    protected T findChildOrFail(String handlerSpec) throws InvalidHandlerSpecException {
        String firstNodeId = findFirstNodeId(handlerSpec);
        T child = children.get(firstNodeId);
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
    public <F extends T> void addChild(F child) {
        Preconditions.checkNotNull(child, "child");
        child.setParent(this);
        children.put(child.getHandlerSpec(), child);
    }

    @Override
    public Collection<T> getChildren() {
        return children.values();
    }
}
