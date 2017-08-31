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
    public SkillHandler createHandler(Skill skill) throws InvalidHandlerSpecException {
        Preconditions.checkNotNull(skill, "skill");
        String relativeSpec = findRelativeSpec(skill);
        T factory = findChildOrFail(relativeSpec);
        return factory.createHandler(skill);
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
                '}';
    }
}
