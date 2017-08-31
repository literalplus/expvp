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

package me.minotopia.expvp.handler.factory.damage;

import li.l1t.common.string.ArgumentFormatException;
import me.minotopia.expvp.api.handler.damage.DamageHandler;
import me.minotopia.expvp.api.handler.factory.DamageHandlerFactory;
import me.minotopia.expvp.api.handler.factory.InvalidHandlerSpecException;
import me.minotopia.expvp.handler.factory.AbstractHandlerSpecNode;
import me.minotopia.expvp.handler.factory.HandlerArgs;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

/**
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public abstract class AbstractDamageHandlerFactory extends AbstractHandlerSpecNode implements DamageHandlerFactory {
    private static final int PROBABILITY_INDEX = 0;
    private static final int EFFECT_INDEX = 1;
    private static final int DURATION_INDEX = 2;
    private static final int LEVEL_INDEX = 3;

    public AbstractDamageHandlerFactory(String ownHandlerSpec) {
        super(ownHandlerSpec);
    }

    @Override
    public DamageHandler createHandler(Skill skill) throws InvalidHandlerSpecException {
        try {
            String relativeSpec = findRelativeSpec(skill);
            return createHandler(skill, new HandlerArgs(relativeSpec));
        } catch (ArgumentFormatException e) {
            throw new InvalidHandlerSpecException(e.getMessage(), skill.getHandlerSpec(), this);
        }
    }

    /**
     * Creates a handler using this damage handler factory's specific creation mechanism.
     *
     * @param skill the skill to create the handler for
     * @param args  the argument list, retrieved from the skill's handler spec, where 1 is the first argument that is
     *              specifically meant for the implementation
     * @return the created kit handler for given arguments
     */
    protected abstract DamageHandler createHandler(Skill skill, HandlerArgs args);

    protected int probabilityPerCent(HandlerArgs args) {
        return args.intArg(PROBABILITY_INDEX);
    }

    protected PotionEffectType potionEffectType(HandlerArgs args) {
        String effectName = args.arg(EFFECT_INDEX).toLowerCase().replaceAll("[ -]", "_");
        PotionEffectType effectType = PotionEffectType.getByName(effectName);
        if (effectType == null) {
            throw new ArgumentFormatException(effectName, EFFECT_INDEX, "one of " + Arrays.toString(PotionEffectType.values()));
        }
        return effectType;
    }

    protected int durationSeconds(HandlerArgs args) {
        return args.intArg(DURATION_INDEX);
    }

    protected int potionLevel(HandlerArgs args) {
        return args.intArg(LEVEL_INDEX);
    }
}
