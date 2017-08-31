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

package me.minotopia.expvp.api.handler.factory;

import java.util.Collection;

/**
 * A skill handler factory that splits handler specs in parts and delegates handler creation to its
 * children based on these parts.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public interface CompoundHandlerFactory<T extends HandlerFactory> extends HandlerFactory {
    /**
     * Registers a handler factory as a child of this compound factory.
     *
     * @param child the child to register
     */
    <F extends T> void addChild(F child);

    /**
     * @return the collection of this factory's children
     */
    Collection<T> getChildren();
}
