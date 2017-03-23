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
 * An effect handler that poisons players that hit a player with this skill, with a random chance.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class PoisonousArmorHandler extends EffectHandler {
    public PoisonousArmorHandler(Skill skill, int probabilityPerCent, int durationSeconds) {
        super(skill, probabilityPerCent, PotionEffectType.POISON, durationSeconds, 1);
    }

    @Override
    public void handleVictim(Player victim, Player culprit) {
        if (isChanceMet()) {
            applyPotionTo(culprit);
        }
    }
}
