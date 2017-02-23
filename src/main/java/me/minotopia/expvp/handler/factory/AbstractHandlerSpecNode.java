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
    private final HandlerSpecNode parent;
    private final String ownHandlerSpec;

    public AbstractHandlerSpecNode(HandlerSpecNode parent, String ownHandlerSpec) {
        this.parent = parent;
        this.ownHandlerSpec = Preconditions.checkNotNull(ownHandlerSpec, "ownHandlerSpec");
    }

    public String getHandlerSpec() {
        return ownHandlerSpec;
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

    protected String findRelativeSpec(Skill skill) {
        Matcher matcher = Pattern.compile(Pattern.quote(this.getFullHandlerSpec()) + "/").matcher(skill.getHandlerSpec());
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
