/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.hit;

import com.google.inject.Inject;
import li.l1t.common.util.DamageHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

/**
 * Listens for hits and records then with {@link MapOutgoingHitService}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public class OutgoingHitListener implements Listener {
    private final MapOutgoingHitService hitService;

    @Inject
    public OutgoingHitListener(MapOutgoingHitService hitService) {
        this.hitService = hitService;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            DamageHelper.findActualDamager(event)
                    .map(Player::getUniqueId)
                    .ifPresent(damagerId -> recordHit(event, damagerId));
        }
    }

    private void recordHit(EntityDamageByEntityEvent event, UUID damagerId) {
        Player victim = (Player) event.getEntity();
        hitService.recordHitOnBy(victim.getUniqueId(), damagerId, event.getFinalDamage());
    }
}
