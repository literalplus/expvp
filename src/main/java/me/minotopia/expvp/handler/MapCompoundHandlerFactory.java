/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.handler.CompoundSkillHandlerFactory;
import me.minotopia.expvp.api.handler.InvalidHandlerSpecException;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.api.handler.SkillHandlerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A compound skill handler factory based on a map.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-14
 */
public class MapCompoundHandlerFactory extends AbstractHandlerSpecNode implements CompoundSkillHandlerFactory {
    private final Map<String, SkillHandlerFactory> children = new HashMap<>();
    private final String separator;

    public MapCompoundHandlerFactory(String ownHandlerSpec, String separator) {
        super(ownHandlerSpec);
        this.separator = Preconditions.checkNotNull(separator, "separator");
    }

    public MapCompoundHandlerFactory withChild(SkillHandlerFactory child) {
        addChild(child);
        return this;
    }

    @Override
    public void addChild(SkillHandlerFactory child) {
        Preconditions.checkNotNull(child, "child");
        children.put(child.getHandlerSpec(), child);
    }

    @Override
    public Collection<SkillHandlerFactory> getChildren() {
        return children.values();
    }

    @Override
    public SkillHandler createHandler(EPPlugin plugin, String handlerSpec) throws InvalidHandlerSpecException {
        Preconditions.checkNotNull(plugin, "plugin");
        Preconditions.checkNotNull(handlerSpec, "handlerSpec");
        SkillHandlerFactory factory = findChildOrFail(handlerSpec);
        return factory.createHandler(plugin, removeFirstNode(factory, handlerSpec));
    }

    private SkillHandlerFactory findChildOrFail(String handlerSpec) throws InvalidHandlerSpecException {
        String firstNodeId = findFirstNodeId(handlerSpec);
        SkillHandlerFactory child = children.get(firstNodeId);
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
        return handlerSpec.split(separator);
    }

    private InvalidHandlerSpecException invalidSpecException(String message, String handlerSpec) {
        return new InvalidHandlerSpecException(
                message + " after '" + getHandlerSpec() + "'", handlerSpec, this
        );
    }

    private String removeFirstNode(SkillHandlerFactory factory, String handlerSpec) {
        return removeFirstSeparator(
                removePrefix(factory.getHandlerSpec(), handlerSpec)
        );
    }

    private String removePrefix(String toRemove, String absoluteHandlerSpec) {
        return absoluteHandlerSpec.replaceFirst(Pattern.quote(toRemove), "");
    }

    private String removeFirstSeparator(String handlerSpecWithoutBase) {
        return handlerSpecWithoutBase.substring(separator.length());
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("compound handler '").append(getHandlerSpec()).append("':\n");
        children.entrySet().forEach(entry -> describeChild(sb, entry));
        return sb.toString();
    }

    private void describeChild(StringBuilder sb, Map.Entry<String, SkillHandlerFactory> entry) {
        SkillHandlerFactory child = entry.getValue();
        sb.append(" - ").append(getHandlerSpec()).append(separator)
                .append(child.getHandlerSpec())
                .append(" -> ").append(child.getDescription());
    }
}
