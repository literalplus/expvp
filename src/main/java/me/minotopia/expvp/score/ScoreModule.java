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

package me.minotopia.expvp.score;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.api.score.assist.KillAssistService;
import me.minotopia.expvp.api.score.hit.OutgoingHitService;
import me.minotopia.expvp.api.score.league.LeagueService;
import me.minotopia.expvp.api.score.points.TalentPointService;
import me.minotopia.expvp.api.score.service.ExpService;
import me.minotopia.expvp.api.score.service.KillDeathService;
import me.minotopia.expvp.api.score.service.KillStreakService;
import me.minotopia.expvp.score.assist.MapKillAssistService;
import me.minotopia.expvp.score.hit.MapOutgoingHitService;
import me.minotopia.expvp.score.hit.OutgoingHitListener;
import me.minotopia.expvp.score.league.StaticLeagueService;
import me.minotopia.expvp.score.listener.ForwardingHitListener;
import me.minotopia.expvp.score.listener.ScoreJoinListener;
import me.minotopia.expvp.score.points.PlayerDataTalentPointService;
import me.minotopia.expvp.score.points.RecentDamageTalentPointChecker;
import me.minotopia.expvp.score.service.PlayerDataExpService;
import me.minotopia.expvp.score.service.PlayerDataKillDeathService;
import me.minotopia.expvp.score.service.PlayerKillStreakService;

/**
 * Injector module for everything related to scoring, including Exp, Talent Points, and Leagues.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
public class ScoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ExpService.class).to(PlayerDataExpService.class);
        bind(TalentPointService.class).to(PlayerDataTalentPointService.class);
        bind(LeagueService.class).to(StaticLeagueService.class);
        bind(KillDeathService.class).to(PlayerDataKillDeathService.class);
        bind(KillStreakService.class).to(PlayerKillStreakService.class);
        bind(ForwardingHitListener.class);
        bind(ScoreJoinListener.class);
        bind(KillAssistService.class).to(MapKillAssistService.class);
        bind(OutgoingHitListener.class);
        bind(OutgoingHitService.class).to(MapOutgoingHitService.class);
        bind(RecentDamageTalentPointChecker.class);
    }
}
