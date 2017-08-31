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

package me.minotopia.expvp.handler.kit.compilation;

import me.minotopia.expvp.api.handler.kit.compilation.KitBaseline;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import org.bukkit.Material;

/**
 * Provides a kit baseline defined at compile time.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public class StaticKitBaseline implements KitBaseline {
    private static final int ARMOR_HELMET_ID = 39;
    private static final int ARMOR_CHESTPLATE_ID = 38;
    private static final int ARMOR_LEGGINGS_ID = 37;
    private static final int ARMOR_BOOTS_ID = 36;
    private static final int HOTBAR_LEFTMOST_ID = 0;

    @Override
    public void baseline(KitCompilation compilation) {
        baselineArmor(compilation);
        baselineHotbar(compilation);
    }

    private void baselineArmor(KitCompilation compilation) {
        compilation.slot(ARMOR_HELMET_ID, Material.LEATHER_HELMET).include();
        compilation.slot(ARMOR_CHESTPLATE_ID, Material.LEATHER_CHESTPLATE).include();
        compilation.slot(ARMOR_LEGGINGS_ID, Material.LEATHER_LEGGINGS).include();
        compilation.slot(ARMOR_BOOTS_ID, Material.LEATHER_BOOTS).include();
    }

    private void baselineHotbar(KitCompilation compilation) {
        compilation.slot(HOTBAR_LEFTMOST_ID, Material.WOOD_SWORD);
    }
}
