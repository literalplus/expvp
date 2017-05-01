/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

import com.google.inject.Inject;
import li.l1t.common.util.DamageHelper;
import me.minotopia.expvp.api.misc.PlayerService;
import me.minotopia.expvp.api.respawn.RespawnService;
import me.minotopia.expvp.api.score.KillDeathService;
import me.minotopia.expvp.api.score.KillStreakService;
import me.minotopia.expvp.api.score.assist.Hit;
import me.minotopia.expvp.api.score.assist.Hits;
import me.minotopia.expvp.api.score.assist.KillAssistService;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Optional;

/**
 * Forwards kills and deaths to KillDeathService.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-02
 */
public class ForwardingHitListener implements Listener {
    private final KillDeathService killDeathService;
    private final RespawnService respawnService;
    private final KillStreakService streakService;
    private final KillAssistService assistService;
    private final PlayerService playerService;

    @Inject
    public ForwardingHitListener(KillDeathService killDeathService, RespawnService respawnService,
                                 KillStreakService streakService, KillAssistService assistService, PlayerService playerService) {
        this.killDeathService = killDeathService;
        this.respawnService = respawnService;
        this.streakService = streakService;
        this.assistService = assistService;
        this.playerService = playerService;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerHit(EntityDamageEvent event) {
        if (isTheVictimAPlayer(event)) {
            Player victim = (Player) event.getEntity();
            Optional<Player> culprit = actualDamager(event);
            Hits hits = assistService.getHitsOn(victim.getUniqueId());
            if (isFatalHit(event, victim)) {
                if (culprit.isPresent()) {
                    killDeathService.onFatalHit(culprit.get(), victim);
                } else {
                    creditKillToMostRecentDamagerIfNotExpired(victim, hits);
                }
                event.setCancelled(true);
                handleFatalHitOn(victim);
            } else {
                culprit.map(Player::getUniqueId)
                        .ifPresent(damagerId -> hits.recordHitBy(damagerId, event.getFinalDamage()));
            }
        }
    }

    private void creditKillToMostRecentDamagerIfNotExpired(Player victim, Hits hits) {
        hits.getMostRecentHitIfNotExpired()
                .map(Hit::getCulpritId)
                .flatMap(playerService::findOnlinePlayer)
                .ifPresent(mostRecentDamager -> killDeathService.onFatalHit(mostRecentDamager, victim));
    }

    private Optional<Player> actualDamager(EntityDamageEvent event) {
        return DamageHelper.findActualDamager(event)
                .filter(culprit -> culprit.getUniqueId() != event.getEntity().getUniqueId());
    }

    private boolean isTheVictimAPlayer(EntityDamageEvent event) {
        return event.getEntityType() == EntityType.PLAYER;
    }

    private boolean isFatalHit(EntityDamageEvent event, Player victim) {
        return (victim.getHealth() - event.getFinalDamage()) <= 0D;
    }

    private void handleFatalHitOn(Player victim) {
        assistService.clearHitsOn(victim.getUniqueId());
        streakService.resetStreak(victim);
        teleportToSpawn(victim);
        restoreHealthEtc(victim);
    }

    private void teleportToSpawn(Player victim) {
        respawnService.startRespawnDelay(victim);
    }

    private void restoreHealthEtc(Player victim) {
        victim.setHealth(victim.getMaxHealth());
        victim.setFoodLevel(20);
        victim.setSaturation(20);
        victim.getActivePotionEffects().stream()
                .map(PotionEffect::getType).forEach(victim::removePotionEffect);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getEntity().spigot().respawn();
        respawnService.startRespawn(event.getEntity());
        event.setDroppedExp(0);
        event.getDrops().clear();
        streakService.resetStreak(event.getEntity());
    }
}
