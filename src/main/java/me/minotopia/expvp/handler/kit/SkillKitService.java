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
