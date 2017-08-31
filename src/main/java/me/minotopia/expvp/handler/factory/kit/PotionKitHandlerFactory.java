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

import me.minotopia.expvp.handler.factory.HandlerArgs;
import me.minotopia.expvp.handler.kit.PotionKitHandler;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

/**
 * A factory for potion kit handlers with effect and level.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-16
 */
public class PotionKitHandlerFactory extends AbstractKitHandlerFactory {
    public static final int EFFECT_INDEX = 2;
    public static final int LEVEL_INDEX = 3;

    public PotionKitHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    @Override
    public String getDescription() {
        return "potion kit handler: slot,amount,effect,level";
    }

    @Override
    protected PotionKitHandler createHandler(Skill skill, HandlerArgs args) {
        return new PotionKitHandler(skill, slotId(args), Material.POTION, args.intArg(1), potionType(args), level(args));
    }

    private PotionType potionType(HandlerArgs args) {
        return args.enumArg(PotionType.class, EFFECT_INDEX);
    }

    private int level(HandlerArgs args) {
        return args.intArg(LEVEL_INDEX);
    }

}
