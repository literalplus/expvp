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
