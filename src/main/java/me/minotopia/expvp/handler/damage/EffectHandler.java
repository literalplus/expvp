/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
