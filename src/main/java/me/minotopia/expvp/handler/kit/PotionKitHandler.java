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
import org.bukkit.potion.PotionType;

/**
 * Represents a potion in a kit.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-16
 */
public class PotionKitHandler extends SimpleKitHandler {
    private final PotionType effect;
    private final int level;

    public PotionKitHandler(Skill skill, int slotId, Material type, int amount, PotionType effect, int level) {
        super(skill, slotId, type, amount);
        this.effect = effect;
        this.level = level;
    }

    @Override
    public void handle(KitCompilation compilation) {
        super.handle(compilation);
        element(compilation).asPotion(effect, level).asSplashPotion();
    }
}
