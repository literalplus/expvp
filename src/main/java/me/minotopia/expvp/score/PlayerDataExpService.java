/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.ExpService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

/**
 * An ExpService implementation that uses PlayerDataService as Exp data source.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
public class PlayerDataExpService implements ExpService {
    private final PlayerDataService players;
    private final SessionProvider sessionProvider;

    @Inject
    public PlayerDataExpService(PlayerDataService players, SessionProvider sessionProvider) {
        this.players = players;
        this.sessionProvider = sessionProvider;
    }

    @Override
    public void incrementExp(Player player, int exp) {
        Preconditions.checkArgument(exp > 0, "Exp can only be incremented by a positive amount, got: ", exp, player);
        modifyExp(player, exp);
    }

    private void modifyExp(Player player, int expModifier) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            MutablePlayerData playerData = players.findOrCreateDataMutable(player.getUniqueId());
            playerData.setExp(playerData.getExp() + expModifier);
            players.saveData(playerData);
            scoped.commitIfLastAndChanged();
        }
    }

    @Override
    public void decrementExp(Player player, int exp) {
        Preconditions.checkArgument(exp > 0, "Exp can only be decremented by a positive amount, got: ", exp, player);
        modifyExp(player, exp * -1);
    }

    @Override
    public int getExpCount(Player player) {
        int expCount;
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            PlayerData playerData = players.findOrCreateData(player.getUniqueId());
            expCount = playerData.getExp();
            scoped.commitIfLastAndChanged();
        }
        return expCount;
    }
}