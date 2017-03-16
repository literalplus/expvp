/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory.kit;

import li.l1t.common.string.ArgumentFormatException;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.handler.kit.EnchantKitHandler;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.enchantments.Enchantment;

/**
 * A factory for enchanting kit handlers with material, enchantment and level.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-16
 */
public class EnchantKitHandlerFactory extends AbstractKitHandlerFactory<EnchantKitHandler> {
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
    protected EnchantKitHandler createHandler(EPPlugin plugin, Skill skill, KitArgs args) {
        return new EnchantKitHandler(skill, slotId(args), material(args), enchantment(args, skill), level(args));
    }

    private Enchantment enchantment(KitArgs args, Skill skill) {
        String enchantmentName = args.arg(ENCHANTMENT_INDEX).toLowerCase().replaceAll("[ -]", "_");
        Enchantment enchantment = Enchantment.getByName(enchantmentName);
        if (enchantment == null) {
            throw new ArgumentFormatException(
                    "unknown enchantment " + enchantmentName, ENCHANTMENT_INDEX,
                    "a known enchantment name from the org.bukkit.enchantments.Enchantment class"
            );
        }
        return enchantment;
    }

    private int level(KitArgs args) {
        return args.intArg(LEVEL_INDEX);
    }

}
