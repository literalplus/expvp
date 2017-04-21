/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.misc;

import com.google.inject.Inject;
import me.minotopia.expvp.ui.menu.MainMenu;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Opens the main menu when a player clicks any villager.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
public class VillagerClickHandler implements Listener {
    private final MainMenu.Factory menuFactory;

    @Inject
    public VillagerClickHandler(MainMenu.Factory menuFactory) {
        this.menuFactory = menuFactory;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.VILLAGER) {
            return;
        }
        menuFactory.openMenuFor(event.getPlayer());
    }
}
