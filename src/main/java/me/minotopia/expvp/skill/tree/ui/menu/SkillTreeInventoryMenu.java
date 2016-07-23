/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.menu;

import li.l1t.common.inventory.gui.SimpleInventoryMenu;
import li.l1t.common.util.inventory.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * An inventory menu that provides the frontend for a skill tree.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-22
 */
public class SkillTreeInventoryMenu extends SimpleInventoryMenu {
    protected SkillTreeInventoryMenu(Plugin plugin, String inventoryTitle, Player player) {
        super(plugin, inventoryTitle, player);
    }

    @Override
    protected ItemStackFactory getPlaceholderFactory() {
        return new ItemStackFactory(Material.IRON_FENCE)
                .displayName("§7§l*");
    }
}
