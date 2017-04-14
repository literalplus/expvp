/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.league;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import li.l1t.common.intake.i18n.Message;
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
class StaticLeagueChangeService {
    private final Logger LOGGER = LoggingManager.getLogger(StaticLeagueChangeService.class);
    private final SessionProvider sessionProvider;
    private final DisplayNameService names;
    private final Map<League, LeagueChanger> leagueChangers = new HashMap<>();
    //can't use EnumMap because that would be Map<StaticLeague, LeagueChanger>, but that doesn't conform to EnumMap<League, LeagueChanger>

    @Inject
    public StaticLeagueChangeService(SessionProvider sessionProvider, DisplayNameService names) {
        this.sessionProvider = sessionProvider;
        this.names = names;
        putDefaultChanger(StaticLeague.WOOD);
        putDefaultChanger(StaticLeague.STONE);
        putDefaultChanger(StaticLeague.EMERALD);
        putDefaultChanger(StaticLeague.DIAMOND);
        leagueChangers.put(StaticLeague.OBSIDIAN, new ObsidianLeagueChanger());
        putDefaultChanger(StaticLeague.BEDROCK);
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
        I18n.sendLoc(player, Format.broadcast(Message.of("score!league.change.down", names.displayName(newLeague))));
    }
}
