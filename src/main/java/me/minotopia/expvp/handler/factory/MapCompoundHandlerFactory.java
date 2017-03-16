/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.handler.HandlerFactoryGraph;
import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.api.handler.factory.HandlerFactory;
import me.minotopia.expvp.api.handler.factory.HandlerSpecNode;
import me.minotopia.expvp.api.handler.factory.InvalidHandlerSpecException;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * A compound skill handler factory based on a map.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public class MapCompoundHandlerFactory<T extends HandlerFactory>
        extends MultiHandlerSpecNode<T, SkillHandler> {
    public MapCompoundHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    public MapCompoundHandlerFactory(HandlerSpecNode parent, String ownHandlerSpec) {
        super(parent, ownHandlerSpec);
    }

    @Override
    public SkillHandler createHandler(EPPlugin plugin, Skill skill) throws InvalidHandlerSpecException {
        Preconditions.checkNotNull(plugin, "plugin");
        Preconditions.checkNotNull(skill, "skill");
        String relativeSpec = findRelativeSpec(skill);
        T factory = findChildOrFail(relativeSpec);
        return factory.createHandler(plugin, skill);
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("compound handler '").append(getHandlerSpec()).append("':\n");
        getChildren().forEach(child -> describeChild(sb, child));
        return sb.toString();
    }

    private void describeChild(StringBuilder sb, HandlerFactory factory) {
        sb.append(" - ").append(getHandlerSpec()).append(HandlerFactoryGraph.SEPARATOR)
                .append(factory.getHandlerSpec())
                .append(" -> ").append(factory.getDescription());
    }

    @Override
    public String toString() {
        return "MapCompoundHandlerFactory{" +
                "handlerSpec=" + getHandlerSpec() +
                ",fullHandlerSpec=" + getFullHandlerSpec() +
                ",children=" + getChildren() +
                '}';
    }
}
