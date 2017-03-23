/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
