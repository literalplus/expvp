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

package me.minotopia.expvp.i18n;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import li.l1t.common.i18n.Message;
import li.l1t.common.shared.uuid.UUIDRepository;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.score.league.LeagueService;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SkillTree;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Resolves display names for Expvp entities.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class EPDisplayNameService implements DisplayNameService {
    private static final Message UNKNOWN = Message.of("core!unknown");
    private final LeagueService leagueService;
    private final UUIDRepository uuidRepository;

    @Inject
    public EPDisplayNameService(LeagueService leagueService, UUIDRepository uuidRepository) {
        this.leagueService = leagueService;
        this.uuidRepository = uuidRepository;
    }

    @Override
    public Message displayName(Skill skill) {
        if (skill == null) {
            return UNKNOWN;
        }
        return Message.of("skill!" + skill.getId() + ".name");
    }

    @Override
    public Message description(Skill skill) {
        if (skill == null) {
            return Message.ofText("");
        }
        return Message.of("skill!" + skill.getId() + ".desc");
    }

    @Override
    public Message displayName(SkillTree tree) {
        if (tree == null) {
            return UNKNOWN;
        }
        return Message.of("tree!" + tree.getId() + ".name");
    }

    @Override
    public Message description(SkillTree tree) {
        if (tree == null) {
            return Message.ofText("");
        }
        return Message.of("tree!" + tree.getId() + ".desc");
    }

    @Override
    public Message displayName(Player player) {
        Preconditions.checkNotNull(player, "player");
        return Message.of("core!player.prefixed-name",
                player.getName(), displayName(leagueService.getCurrentLeague(player))
        );
    }

    @Override
    public Message displayName(PlayerData data) {
        Preconditions.checkNotNull(data, "data");
        return Optional.ofNullable(uuidRepository.getName(data.getUniqueId()))
                .map(name -> Message.of("core!player.prefixed-name",
                        name, displayName(leagueService.getPlayerLeague(data))
                )).orElse(UNKNOWN);
    }

    @Override
    public Message displayName(League league) {
        if (league == null) {
            return UNKNOWN;
        }
        return Message.of("score!league." + league.name() + ".name");
    }

    @Override
    public Message chatFormat(League league, String message) {
        if (league == null) {
            return UNKNOWN;
        }
        return Message.of("score!league." + league.name() + ".chat-format", message);
    }

    @Override
    public Message displayName(MapSpawn spawn) {
        if (spawn == null) {
            return UNKNOWN;
        }
        return Message.of("spawn!" + spawn.getId() + ".name");
    }
}
