/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
