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

import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.entity.Player;

/**
 * A handler that heals a skilled victim of a hit with a random chance.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class VictimHealHandler extends DamageHandlerAdapter {
    private final double healthPointsHealed;

    public VictimHealHandler(Skill skill, int probabilityPerCent, double healthPointsHealed) {
        super(skill, probabilityPerCent);
        this.healthPointsHealed = healthPointsHealed;
    }

    @Override
    public void handleVictim(Player victim, Player culprit) {
        double targetHealth = victim.getHealth() + healthPointsHealed;
        if (isChanceMet()) {
            if (targetHealth <= victim.getMaxHealth()) {
                victim.setHealth(targetHealth);
            } else {
                victim.setHealth(victim.getMaxHealth());
            }
        }
    }
}
