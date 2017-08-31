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

package me.minotopia.expvp.handler.damage;

import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Regenerates all health immediately if a skilled player falls below a specified amount of health and a chance is met.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
public class NotTodayHandler extends DamageHandlerAdapter {
    private final int minHP;
    private final Set<UUID> belowThresholdPlayers = new HashSet<>();

    public NotTodayHandler(Skill skill, int probabilityPerCent, int minHP, PlayerInitService initService) {
        super(skill, probabilityPerCent);
        this.minHP = minHP;
        initService.registerDeInitHandler(player -> belowThresholdPlayers.remove(player.getUniqueId()));
    }

    @Override
    public void handleVictim(Player victim, Player culprit) {
        if (victim.getHealth() <= minHP) {
            if (belowThresholdPlayers.add(victim.getUniqueId()) && isChanceMet()) {
                victim.setHealth(victim.getMaxHealth());
            }
        } else {
            belowThresholdPlayers.remove(victim.getUniqueId());
        }
    }
}
