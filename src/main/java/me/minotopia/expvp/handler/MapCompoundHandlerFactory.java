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
import me.minotopia.expvp.api.handler.HandlerGraph;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.api.handler.factory.CompoundSkillHandlerFactory;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;
import me.minotopia.expvp.api.handler.factory.InvalidHandlerSpecException;
import me.minotopia.expvp.api.handler.factory.SkillHandlerFactory;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A compound skill handler factory based on a map.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public class MapCompoundHandlerFactory extends AbstractHandlerSpecNode implements CompoundSkillHandlerFactory {
    private final Map<String, SkillHandlerFactory> children = new HashMap<>();

    public MapCompoundHandlerFactory(HandlerSpecNode parent, String ownHandlerSpec) {
        super(parent, ownHandlerSpec);
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
    public SkillHandler createHandler(EPPlugin plugin, Skill skill) throws InvalidHandlerSpecException {
        Preconditions.checkNotNull(plugin, "plugin");
        Preconditions.checkNotNull(skill, "skill");
        String relativeSpec = findRelativeSpec(skill);
        SkillHandlerFactory factory = findChildOrFail(relativeSpec);
        return factory.createHandler(plugin, skill);
    }

    private String findRelativeSpec(Skill skill) {
        Matcher matcher = Pattern.compile(Pattern.quote(this.getFullHandlerSpec()) + "/").matcher(skill.getHandlerSpec());
        if (matcher.find()) {
            return matcher.replaceFirst("");
        } else {
            throw invalidSpecException("Unable to find relative spec - is the input a sub-spec of this factory '" +
                    this.getFullHandlerSpec() + "'?", skill.getHandlerSpec());
        }
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
        return handlerSpec.split(HandlerGraph.SEPARATOR);
    }

    private InvalidHandlerSpecException invalidSpecException(String message, String handlerSpec) {
        return new InvalidHandlerSpecException(
                message + " after '" + getHandlerSpec() + "'", handlerSpec, this
        );
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
        sb.append(" - ").append(getHandlerSpec()).append(HandlerGraph.SEPARATOR)
                .append(child.getHandlerSpec())
                .append(" -> ").append(child.getDescription());
    }

    @Override
    public String toString() {
        return "MapCompoundHandlerFactory{" +
                "handlerSpec=" + getHandlerSpec() +
                ",fullHandlerSpec=" + getFullHandlerSpec() +
                ",children=" + children +
                '}';
    }
}
