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
