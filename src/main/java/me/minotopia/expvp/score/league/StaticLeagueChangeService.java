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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.score.league.LeagueChanger;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.logging.LoggingManager;
import me.minotopia.expvp.util.SessionProvider;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Takes care of actually changing leagues after it has been determined that a league change is necessary.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-30
 */
@Singleton
class StaticLeagueChangeService {
    private final Logger LOGGER = LoggingManager.getLogger(StaticLeagueChangeService.class);
    private final SessionProvider sessionProvider;
    private final DisplayNameService names;
    private final Map<League, LeagueChanger> leagueChangers = new HashMap<>();
    //can't use EnumMap because that would be Map<StaticLeague, LeagueChanger>, but that doesn't conform to EnumMap<League, LeagueChanger>
    private final LeagueChangeDisplayQueue leagueChangeDisplayQueue;

    @Inject
    public StaticLeagueChangeService(SessionProvider sessionProvider, DisplayNameService names,
                                     LeagueChangeDisplayQueue leagueChangeDisplayQueue, Top5Service top5Service) {
        this.sessionProvider = sessionProvider;
        this.names = names;
        this.leagueChangeDisplayQueue = leagueChangeDisplayQueue;
        putDefaultChanger(StaticLeague.WOOD);
        putDefaultChanger(StaticLeague.STONE);
        putDefaultChanger(StaticLeague.EMERALD);
        putDefaultChanger(StaticLeague.DIAMOND);
        leagueChangers.put(StaticLeague.OBSIDIAN, new ObsidianLeagueChanger(top5Service));
        leagueChangers.put(StaticLeague.BEDROCK, new BedrockLeagueChanger(top5Service));
    }

    private void putDefaultChanger(StaticLeague league) {
        leagueChangers.put(league, new ExpLeagueChanger(league));
    }

    public void updateLeagueIfNecessary(Player player, MutablePlayerData playerData, League league) {
        LeagueChanger changer = leagueChangers.get(league);
        if (changer == null) {
            LOGGER.warn("No league changer for {} at {}", league, player.getName());
            return;
        }
        if (changer.needsLeagueChangeUp(playerData)) {
            LOGGER.debug("Changing league up from {} for {}", league, player.getName());
            league.next().ifPresent(nextLeague -> changeLeagueUp(player, playerData, nextLeague));
        } else if (changer.needsLeagueChangeDown(playerData)) {
            LOGGER.debug("Changing league down from {} for {}", league, player.getName());
            league.previous().ifPresent(nextLeague -> changeLeagueDown(player, playerData, nextLeague));
        }
    }

    public void changeLeagueUp(Player player, MutablePlayerData playerData, League newLeague) {
        sessionProvider.inSession(ignored -> {
            setPlayerDataLeague(playerData, newLeague);
        });
        leagueChangeDisplayQueue.queueLeagueChange(player.getUniqueId());
        I18n.sendLoc(player, Format.broadcast(Message.of("score!league.change.up", names.displayName(newLeague))));
    }

    private void setPlayerDataLeague(MutablePlayerData playerData, League newLeague) {
        Preconditions.checkNotNull(newLeague, "newLeague");
        playerData.setLeagueName(newLeague.name());
    }

    public void changeLeagueDown(Player player, MutablePlayerData playerData, League newLeague) {
        sessionProvider.inSession(ignored -> {
            setPlayerDataLeague(playerData, newLeague);
        });
        leagueChangeDisplayQueue.unqueueLeagueChange(player.getUniqueId());
        I18n.sendLoc(player, Format.broadcast(Message.of("score!league.change.down", names.displayName(newLeague))));
    }
}
