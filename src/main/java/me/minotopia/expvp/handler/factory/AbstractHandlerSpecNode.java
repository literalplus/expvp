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
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;
import me.minotopia.expvp.api.handler.factory.InvalidHandlerSpecException;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Abstract base class for handler spec nodes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-02-23
 */
public class AbstractHandlerSpecNode implements HandlerSpecNode {
    private final String ownHandlerSpec;
    private HandlerSpecNode parent;
    private Pattern fullHandlerSpecPattern;

    public AbstractHandlerSpecNode(HandlerSpecNode parent, String ownHandlerSpec) {
        this.parent = parent;
        this.ownHandlerSpec = Preconditions.checkNotNull(ownHandlerSpec, "ownHandlerSpec");
    }

    public AbstractHandlerSpecNode(String ownHandlerSpec) {
        this(null, ownHandlerSpec);
    }

    public String getHandlerSpec() {
        return ownHandlerSpec;
    }

    @Override
    public void setParent(HandlerSpecNode parent) {
        this.parent = parent;
    }

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
                .collect(Collectors.joining(HandlerFactoryGraph.SEPARATOR));
    }

    private Pattern findFullHandlerSpecPattern() {
        if (fullHandlerSpecPattern == null) {
            fullHandlerSpecPattern = Pattern.compile(Pattern.quote(this.getFullHandlerSpec()) + "/");
        }
        return fullHandlerSpecPattern;
    }

    protected String findRelativeSpec(Skill skill) {
        Matcher matcher = findFullHandlerSpecPattern().matcher(skill.getHandlerSpec());
        if (matcher.find()) {
            return matcher.replaceFirst("");
        } else {
            throw invalidSpecException("Unable to find relative spec - is the input a sub-spec of this factory '" +
                    this.getFullHandlerSpec() + "'?", skill.getHandlerSpec());
        }
    }

    protected InvalidHandlerSpecException invalidSpecException(String message, String handlerSpec) {
        return new InvalidHandlerSpecException(
                message + " after '" + getHandlerSpec() + "'", handlerSpec, this
        );
    }
}
