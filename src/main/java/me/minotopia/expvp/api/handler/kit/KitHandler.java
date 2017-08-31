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

package me.minotopia.expvp.api.handler.kit;

import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import org.bukkit.Material;

/**
 * A skill handler which mutates a player's kit during compilation.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
public interface KitHandler extends SkillHandler {
    /**
     * Handles the compilation of a kit element and mutates the state of the current element
     * according to this handler.
     *
     * @param compilation the compilation to handle
     * @throws IllegalStateException if the compilation does not have a current element
     */
    void handle(KitCompilation compilation);

    /**
     * @return the amount of items this handler adds to the stack
     */
    int getAmount();

    /**
     * @return the type of items this handler adds to the stack
     */
    Material getType();

    /**
     * @return the slot id this handler handles items for
     */
    int getSlotId();
}
