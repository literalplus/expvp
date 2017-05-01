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

/**
 * Makes an item a Notch's Golden Apple.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-16
 */
public class NotchAppleKitHandler extends SimpleKitHandler {
    public NotchAppleKitHandler(Skill skill, int slotId, int amount) {
        super(skill, slotId, Material.GOLDEN_APPLE, amount);
    }

    @Override
    public void handle(KitCompilation compilation) {
        element(compilation).addToAmount(getAmount())
                .factory().getBase().setDurability((short) 1);
    }
}
