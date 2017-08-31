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

package me.minotopia.expvp.model;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.api.model.RankService;
import me.minotopia.expvp.api.model.friend.FriendRequestRepository;
import me.minotopia.expvp.api.model.friend.FriendshipRepository;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.model.friend.HibernateFriendRequestRepository;
import me.minotopia.expvp.model.friend.HibernateFriendshipRepository;
import me.minotopia.expvp.model.player.HibernatePlayerDataService;
import me.minotopia.expvp.model.player.HibernatePlayerTopRepository;
import me.minotopia.expvp.model.player.HibernateResetService;
import me.minotopia.expvp.model.score.HibernateRankService;

/**
 * Provides the dependency wiring for the model module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-09
 */
public class ModelModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PlayerDataService.class).to(HibernatePlayerDataService.class);
        bind(HibernateResetService.class);
        bind(FriendshipRepository.class).to(HibernateFriendshipRepository.class);
        bind(FriendRequestRepository.class).to(HibernateFriendRequestRepository.class);
        bind(RankService.class).to(HibernateRankService.class);
        bind(HibernateResetService.class);
        bind(HibernatePlayerTopRepository.class);
    }
}
