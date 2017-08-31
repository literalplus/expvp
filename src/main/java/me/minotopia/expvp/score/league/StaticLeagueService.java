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

package me.minotopia.expvp.score.league;

import com.google.inject.Inject;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.score.league.LeagueService;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Assigns players to the appropriate leagues and distributes assignment information.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-29
 */
public class StaticLeagueService implements LeagueService {
    private final PlayerDataService players;
    private final SessionProvider sessionProvider;
    private final StaticLeagueChangeService changeService;

    @Inject
    public StaticLeagueService(PlayerDataService players, SessionProvider sessionProvider,
                               StaticLeagueChangeService changeService) {
        this.players = players;
        this.sessionProvider = sessionProvider;
        this.changeService = changeService;
    }

    @Override
    public League getCurrentLeague(Player player) {
        return players.findData(player.getUniqueId())
                .map(this::getPlayerLeague)
                .orElse(StaticLeague.getDefaultLeague());
    }

    @Override
    public League getPlayerLeague(PlayerData playerData) {
        return getLeague(playerData.getLeagueName())
                .orElseGet(StaticLeague::getDefaultLeague);
    }

    @Override
    public Optional<League> getLeague(String leagueName) {
        if (leagueName == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(StaticLeague.valueOf(leagueName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateLeague(Player player) {
        sessionProvider.inSession(ignored -> {
            MutablePlayerData data = players.findOrCreateDataMutable(player.getUniqueId());
            changeService.updateLeagueIfNecessary(player, data, getPlayerLeague(data));
        });
    }
}
