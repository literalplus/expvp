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

package me.minotopia.expvp.score.points;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.hit.OutgoingHitService;
import me.minotopia.expvp.api.score.points.TalentPointObjective;
import me.minotopia.expvp.api.score.points.TalentPointService;
import me.minotopia.expvp.api.score.points.TalentPointType;
import me.minotopia.expvp.api.score.points.TalentPointTypeStrategy;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.score.points.strategy.DayActivityTalentPointStrategy;
import me.minotopia.expvp.score.points.strategy.DeathsTalentPointStrategy;
import me.minotopia.expvp.score.points.strategy.KillsTalentPointStrategy;
import me.minotopia.expvp.score.points.strategy.RecentDamageTalentPointStrategy;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

import java.util.EnumMap;

/**
 * Works out Talent Points using the PlayerData API.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
public class PlayerDataTalentPointService implements TalentPointService {
    private final PlayerDataService players;
    private final SessionProvider sessionProvider;
    private final TalentPointCalculator calculator;
    private final TalentPointDisplayService displayService;
    private final EnumMap<TalentPointType, TalentPointTypeStrategy> strategies = new EnumMap<>(TalentPointType.class);

    @Inject
    public PlayerDataTalentPointService(PlayerDataService players, SessionProvider sessionProvider,
                                        TalentPointCalculator calculator, OutgoingHitService hitService) {
        this.players = players;
        this.sessionProvider = sessionProvider;
        this.calculator = calculator;
        displayService = new TalentPointDisplayService(this);
        strategies.put(TalentPointType.COMBAT, new KillsTalentPointStrategy());
        strategies.put(TalentPointType.DEATHS, new DeathsTalentPointStrategy());
        strategies.put(TalentPointType.RECENT_DAMAGE, new RecentDamageTalentPointStrategy(hitService));
        strategies.put(TalentPointType.DAY_ACTIVITY, new DayActivityTalentPointStrategy());
    }

    private TalentPointTypeStrategy strategy(TalentPointType type) {
        Preconditions.checkNotNull(type, "type");
        TalentPointTypeStrategy strategy = strategies.get(type);
        Preconditions.checkArgument(strategy != null, "Unsupported talent point type %s", type);
        return strategy;
    }

    @Override
    public int getCurrentTalentPointCount(Player player) {
        try (ScopedSession ignored = sessionProvider.scoped().join()) {
            return players.findData(player.getUniqueId())
                    .map(PlayerData::getAvailableTalentPoints)
                    .orElse(0);
        }
    }

    @Override
    @Deprecated
    public int findTalentPointLimit(Player player) {
        return TalentPointType.COMBAT.getLimit();
    }

    @Override
    @Deprecated
    public boolean hasReachedTalentPointLimit(Player player) {
        Integer kills = players.findData(player.getUniqueId())
                .map(PlayerData::getCurrentKills)
                .orElse(0);
        return calculator.totalDeservedPoints(kills) >= findTalentPointLimit(player);
    }

    @Override
    @Deprecated
    public void grantTalentPoints(Player player, int talentPoints) {
        Preconditions.checkArgument(talentPoints >= 0, "talentPoints must be positive", talentPoints);
        if (talentPoints == 0) {
            return;
        }
        sessionProvider.inSession(ignored -> {
            MutablePlayerData data = players.findOrCreateDataMutable(player.getUniqueId());
            data.setTalentPoints(data.getAvailableTalentPoints() + talentPoints);
            players.saveData(data);
        });
    }

    @Override
    public int grantDeservedTalentPoints(Player player, TalentPointType type) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(player, "player");
        return sessionProvider.inSessionAnd(ignored -> {
            MutablePlayerData playerData = players.findOrCreateDataMutable(player.getUniqueId());
            int deservedPoints = strategy(type).findDeservedPoints(playerData);
            if (deservedPoints > 0) {
                playerData.grantTalentPoints(type, deservedPoints);
                displayService.displayTPGained(player, deservedPoints, type);
                players.saveData(playerData);
            }
            return deservedPoints;
        });
    }

    @Override
    public void consumeTalentPoints(Player player, int consumePoints) {
        sessionProvider.inSession(ignored -> {
            MutablePlayerData playerData = players.findOrCreateDataMutable(player.getUniqueId());
            playerData.consumeTalentPoints(consumePoints);
            displayService.displayTPSpent(player, consumePoints);
            players.saveData(playerData);
        });
    }

    @Override
    public TalentPointObjective nextPointObjective(Player player, TalentPointType type) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(player, "player");
        return sessionProvider.inSessionAnd(ignored -> {
            MutablePlayerData playerData = players.findOrCreateDataMutable(player.getUniqueId());
            return strategy(type).calculateObjectiveForNext(playerData);
        });
    }
}
