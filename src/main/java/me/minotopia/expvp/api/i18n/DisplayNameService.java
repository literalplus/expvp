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

package me.minotopia.expvp.api.i18n;

import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.score.league.League;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SkillTree;
import org.bukkit.entity.Player;

/**
 * Figures out display names and descriptions for different Expvp entities.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public interface DisplayNameService {
    /**
     * @param skill the skill to display
     * @return a message object representing given skill's display name
     */
    Message displayName(Skill skill);

    Message description(Skill skill);

    /**
     * @param tree the tree to display
     * @return a message object representing given tree's display name
     */
    Message displayName(SkillTree tree);

    Message description(SkillTree tree);

    /**
     * @param player the player whose display name to compute
     * @return a message object representing given player's display name
     */
    Message displayName(Player player);

    Message displayName(PlayerData data);

    Message displayName(League league);

    Message chatFormat(League league, String message);

    Message displayName(MapSpawn spawn);
}
