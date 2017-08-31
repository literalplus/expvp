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

package me.minotopia.expvp.api.skill;

import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SkillTreeNode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Provides an API for accessing skills.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public interface SkillService {
    /**
     * Gets the model skills obtained by given player data object.
     *
     * @param playerData the player data to retrieve skill ids from
     * @return the collection of skills given player data has obtained
     */
    Collection<Skill> getSkills(PlayerData playerData);

    Collection<Skill> getAllSkills();

    ItemStack createSkillIconFor(SkillTreeNode<?> node, Player player);

    ItemStackFactory createRawSkillIconFor(Skill skill, boolean obtained, CommandSender receiver);
}
