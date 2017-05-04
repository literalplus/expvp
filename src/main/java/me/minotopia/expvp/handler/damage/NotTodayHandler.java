/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
