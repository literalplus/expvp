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

package me.minotopia.expvp.handler.kit;

import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

/**
 * Enchants a kit item.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-16
 */
public class EnchantKitHandler extends SimpleKitHandler {
    private final Enchantment enchantment;
    private final int level;

    public EnchantKitHandler(Skill skill, int slotId, Material type, Enchantment enchantment, int level) {
        super(skill, slotId, type, 0);
        this.enchantment = enchantment;
        this.level = level;
    }

    @Override
    public void handle(KitCompilation compilation) {
        element(compilation).include()
                .withEnchantment(enchantment, level);
    }
}
