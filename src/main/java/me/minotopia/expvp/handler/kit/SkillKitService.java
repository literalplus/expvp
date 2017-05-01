/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.kit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.minotopia.expvp.api.handler.HandlerService;
import me.minotopia.expvp.api.handler.kit.KitService;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompiler;
import me.minotopia.expvp.api.handler.kit.compilation.KitElement;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Creates player kits from their skill sets.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
@ConstructOnEnable
@Singleton
public class SkillKitService implements KitService {
    private final KitCompiler compiler;
    private final PlayerDataService players;
    private final HandlerService handlerService;
    private final SessionProvider sessionProvider;

    @Inject
    public SkillKitService(KitCompiler compiler, PlayerDataService players, HandlerService handlerService,
                           SessionProvider sessionProvider, PlayerInitService initService) {
        this.compiler = compiler;
        this.players = players;
        this.handlerService = handlerService;
        this.sessionProvider = sessionProvider;
        initService.registerInitHandler(this::applyKit);
    }

    @Override
    public void applyKit(Player player) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            PlayerData data = players.findOrCreateData(player.getUniqueId());
            KitCompilation kit = compiler.compile(player, data);
            applyCompilation(player, kit);
            scoped.commitIfLastAndChanged();
        }
    }

    private void applyCompilation(Player player, KitCompilation kit) {
        player.getInventory().clear();
        kit.getResult().forEach((key, value) -> applyItem(player, key, value));
    }

    private void applyItem(Player player, int slotId, KitElement element) {
        player.getInventory().setItem(slotId, element.toItemStack());
    }

    @Override
    public void invalidateCache(UUID playerId) {
        sessionProvider.inSession(ignored -> {
            players.findData(playerId).ifPresent(data -> {
                handlerService.unregisterHandlers(data);
                handlerService.registerHandlers(data);
            });
        });
    }
}
