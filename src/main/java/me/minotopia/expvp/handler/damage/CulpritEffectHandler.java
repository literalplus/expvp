/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.damage;

import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Handler that applies a potion effect to a skilled player when they hit somebody else, with a random chance.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class CulpritEffectHandler extends EffectHandler {
    public CulpritEffectHandler(Skill skill, int probabilityPerCent, PotionEffectType potionType, int durationSeconds, int potionLevel) {
        super(skill, probabilityPerCent, potionType, durationSeconds, potionLevel);
    }

    @Override
    public void handleCulprit(Player culprit, Player victim) {
        if (isChanceMet()) {
            applyPotionTo(culprit);
        }
    }
}
