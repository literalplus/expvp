/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import li.l1t.common.intake.i18n.Message;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.score.league.LeagueService;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SkillTree;
import org.bukkit.entity.Player;

/**
 * Resolves display names for Expvp entities.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class EPDisplayNameService implements DisplayNameService {
    private final LeagueService leagueService;

    @Inject
    public EPDisplayNameService(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @Override
    public Message displayName(Skill skill) {
        return Message.of("skill!" + skill.getId() + ".name");
    }

    @Override
    public Message description(Skill skill) {
        return Message.of("skill!" + skill.getId() + ".desc");
    }

    @Override
    public Message displayName(SkillTree tree) {
        return Message.of("tree!" + tree.getId() + ".name");
    }

    @Override
    public Message description(SkillTree tree) {
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
    public Message displayName(League league) {
        Preconditions.checkNotNull(league, "league");
        return Message.of("score!league." + league.name() + ".name");
    }

    @Override
    public Message displayName(MapSpawn spawn) {
        Preconditions.checkNotNull(spawn, "spawn");
        return Message.of("spawn!" + spawn.getId() + ".name");
    }
}
