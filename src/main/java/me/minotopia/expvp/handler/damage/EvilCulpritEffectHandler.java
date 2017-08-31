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
import org.bukkit.potion.PotionEffectType;

/**
 * Handler that applies a potion effect to the hit player when they are hit by a skilled player, with a random chance.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class EvilCulpritEffectHandler extends EffectHandler {
    public EvilCulpritEffectHandler(Skill skill, int probabilityPerCent, PotionEffectType potionType, int durationSeconds, int potionLevel) {
        super(skill, probabilityPerCent, potionType, durationSeconds, potionLevel);
    }

    @Override
    public void handleCulprit(Player culprit, Player victim) {
        if (isChanceMet()) {
            applyPotionTo(victim);
        }
    }
}
