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

package me.minotopia.expvp.handler.factory.kit;

import li.l1t.common.string.ArgumentFormatException;
import me.minotopia.expvp.handler.factory.HandlerArgs;
import me.minotopia.expvp.handler.kit.EnchantKitHandler;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.enchantments.Enchantment;

/**
 * A factory for enchanting kit handlers with material, enchantment and level.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-16
 */
public class EnchantKitHandlerFactory extends AbstractKitHandlerFactory {
    public static final int ENCHANTMENT_INDEX = 2;
    public static final int LEVEL_INDEX = 3;

    public EnchantKitHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    @Override
    public String getDescription() {
        return "enchant kit handler: slot,material,enchantment,level";
    }

    @Override
    protected EnchantKitHandler createHandler(Skill skill, HandlerArgs args) {
        return new EnchantKitHandler(skill, slotId(args), material(args), enchantment(args), level(args));
    }

    private Enchantment enchantment(HandlerArgs args) {
        String enchantmentName = args.arg(ENCHANTMENT_INDEX).toUpperCase().replaceAll("[ -]", "_");
        Enchantment enchantment = Enchantment.getByName(enchantmentName);
        if (enchantment == null) {
            throw new ArgumentFormatException(
                    "unknown enchantment " + enchantmentName, ENCHANTMENT_INDEX,
                    "a known enchantment name from the org.bukkit.enchantments.Enchantment class"
            );
        }
        return enchantment;
    }

    private int level(HandlerArgs args) {
        return args.intArg(LEVEL_INDEX);
    }

}
