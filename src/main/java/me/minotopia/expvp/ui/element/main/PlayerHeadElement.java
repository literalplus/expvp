/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.element.main;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * A menu element that shows a player's head.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-20
 */
public class PlayerHeadElement implements MenuElement {
    private final String targetName;
    private final UUID senderId;

    public PlayerHeadElement(Player player, String targetName) {
        this.targetName = targetName;
        this.senderId = player.getUniqueId();
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        ItemStackFactory factory = new ItemStackFactory(Material.SKULL_ITEM);
        if (targetName != null) {
            factory.displayName(targetName).skullOwner(targetName);
        } else {
            factory.displayName("§c???").lore(I18n.loc(senderId, "core!inv.main.no-friend"));
        }
        return factory.produce();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        //TODO: https://bugs.l1t.li/view.php?id=794
    }
}
