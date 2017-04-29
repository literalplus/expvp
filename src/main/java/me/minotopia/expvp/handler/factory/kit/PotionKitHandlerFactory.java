/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
