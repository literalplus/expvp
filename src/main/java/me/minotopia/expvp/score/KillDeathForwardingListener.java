/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

import com.google.inject.Inject;
import me.minotopia.expvp.api.respawn.RespawnService;
import me.minotopia.expvp.api.score.KillDeathService;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;

/**
 * Forwards kills and deaths to KillDeathService.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-02
 */
public class KillDeathForwardingListener implements Listener {
    private final KillDeathService killDeathService;
    private final RespawnService respawnService;

    @Inject
    public KillDeathForwardingListener(KillDeathService killDeathService, RespawnService respawnService) {
        this.killDeathService = killDeathService;
        this.respawnService = respawnService;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (isTheVictimAPlayer(event)) {
            Player victim = (Player) event.getEntity();
            if (isFatalHit(event, victim)) {
                event.setCancelled(true);
                handleFatalHit(victim, event.getDamager());
            }
        }
    }

    private boolean isTheVictimAPlayer(EntityDamageByEntityEvent event) {
        return event.getEntityType() == EntityType.PLAYER;
    }

    private boolean isFatalHit(EntityDamageByEntityEvent event, Player victim) {
        return (victim.getHealth() - event.getFinalDamage()) <= 0D;
    }

    private void handleFatalHit(Player victim, Entity culprit) {
        teleportToSpawn(victim);
        restoreHealthEtc(victim);
        if (isAPlayer(culprit)) {
            killDeathService.onFatalHit((Player) culprit, victim);
        }
    }

    private boolean isAPlayer(Entity entity) {
        return entity != null && entity.getType() == EntityType.PLAYER;
    }

    private void teleportToSpawn(Player victim) {
        victim.sendMessage("This is the point where you'd be teleported to spawn if that was already implemented.");
        //FIXME: Teleport to spawn
        //TODO: this as a callback after teleport
        respawnService.startPreRespawn(victim);
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
        handleFatalHit(event.getEntity(), event.getEntity().getKiller());
    }
}
