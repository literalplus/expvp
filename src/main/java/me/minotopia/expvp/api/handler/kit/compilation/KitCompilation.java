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

package me.minotopia.expvp.api.handler.kit.compilation;

import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Stores the mutable state of an ongoing kit compilation.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
public interface KitCompilation {
    /**
     * @return the player this compilation is for
     */
    Player getTargetPlayer();

    /**
     * @return the player data of the compilation target
     */
    PlayerData getTargetData();

    /**
     * @return the current state of the result, ready to be put into an inventory
     */
    Map<Integer, KitElement> getResult();

    /**
     * @param slotId the slot id to get the builder for
     * @param type   the expected material for the builder
     * @return the builder in given slot, or a new builder if there is a builder of a different type in given slot
     */
    KitElementBuilder slot(int slotId, Material type);

    KitElement snapshotOf(int slotId);
}
