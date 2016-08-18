/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.menu;

import li.l1t.common.inventory.gui.InventoryMenu;
import me.minotopia.expvp.EPPlugin;

/**
 * An inventory menu that is linked to the Expvp plugin.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-18
 */
public interface EPMenu extends InventoryMenu {
    @Override
    EPPlugin getPlugin();
}
