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

import com.google.common.base.Preconditions;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Base class for damage handlers that provide potion effects.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public abstract class EffectHandler extends DamageHandlerAdapter {
    private final PotionEffect potion;

    public EffectHandler(Skill skill, int probabilityPerCent, PotionEffectType potionType, int durationSeconds, int potionLevel) {
        super(skill, probabilityPerCent);
        Preconditions.checkArgument(potionLevel > 0 && potionLevel <= 255, "potionLevel must be between one and 255", potionLevel);
        Preconditions.checkArgument(durationSeconds > 0, "durationSeconds must be positive", durationSeconds);
        this.potion = new PotionEffect(potionType, durationSeconds * 20, potionLevel - 1, true);
    }

    protected void applyPotionTo(LivingEntity entity) {
        potion.apply(entity);
    }
}
