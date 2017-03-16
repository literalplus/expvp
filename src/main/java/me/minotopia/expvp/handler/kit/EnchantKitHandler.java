/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
