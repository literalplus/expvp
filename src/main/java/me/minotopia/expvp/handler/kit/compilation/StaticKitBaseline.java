/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.kit.compilation;

import me.minotopia.expvp.api.handler.kit.compilation.KitBaseline;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import org.bukkit.Material;

/**
 * Provides a kit baseline defined at compile time.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public class StaticKitBaseline implements KitBaseline {
    private static final int ARMOR_HELMET_ID = 39;
    private static final int ARMOR_CHESTPLATE_ID = 38;
    private static final int ARMOR_LEGGINGS_ID = 37;
    private static final int ARMOR_BOOTS_ID = 36;
    private static final int HOTBAR_LEFTMOST_ID = 0;

    @Override
    public void baseline(KitCompilation compilation) {
        baselineArmor(compilation);
        baselineHotbar(compilation);
    }

    private void baselineArmor(KitCompilation compilation) {
        compilation.slot(ARMOR_HELMET_ID, Material.LEATHER_HELMET).include();
        compilation.slot(ARMOR_CHESTPLATE_ID, Material.LEATHER_CHESTPLATE).include();
        compilation.slot(ARMOR_LEGGINGS_ID, Material.LEATHER_LEGGINGS).include();
        compilation.slot(ARMOR_BOOTS_ID, Material.LEATHER_BOOTS).include();
    }

    private void baselineHotbar(KitCompilation compilation) {
        compilation.slot(HOTBAR_LEFTMOST_ID, Material.WOOD_SWORD);
    }
}
