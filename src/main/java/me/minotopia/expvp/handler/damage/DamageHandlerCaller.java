/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.damage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.handler.HandlerService;
import me.minotopia.expvp.api.handler.damage.DamageHandler;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

/**
 * Calls damage handlers for damage events.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
@Singleton
public class DamageHandlerCaller implements Listener {
    private final HandlerService handlerService;
    private final SessionProvider sessionProvider;
    private final PlayerDataService players;

    @Inject
    public DamageHandlerCaller(HandlerService handlerService, SessionProvider sessionProvider, PlayerDataService players, Plugin plugin) {
        this.handlerService = handlerService;
        this.sessionProvider = sessionProvider;
        this.players = players;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        callHandlers((Player) event.getEntity(), (Player) event.getDamager());
    }

    public void callHandlers(Player victim, Player culprit) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            PlayerData victimData = players.findOrCreateData(victim.getUniqueId());
            PlayerData culpritData = players.findOrCreateData(culprit.getUniqueId());
            handlerService.handlersOfTypeStream(DamageHandler.class, victimData)
                    .forEach(handler -> handler.handleVictim(victim, culprit));
            handlerService.handlersOfTypeStream(DamageHandler.class, culpritData)
                    .forEach(handler -> handler.handleCulprit(culprit, victim));
            scoped.commitIfLastAndChanged();
        }
    }
}
