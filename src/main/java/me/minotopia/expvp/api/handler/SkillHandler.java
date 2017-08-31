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

package me.minotopia.expvp.api.handler;

import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * Takes care of applying the effects of a skill to a player.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
public interface SkillHandler {
    /**
     * Enables this handler, preparing everything necessary for it to work and be called.
     *
     * @param plugin the plugin enabling the handler
     */
    void enable(EPPlugin plugin);

    /**
     * Disables this handler, cleaning up the mess it made.
     *
     * @param plugin the plugin disabling the handler
     */
    void disable(EPPlugin plugin);

    /**
     * @return the skill this handler handles
     */
    Skill getSkill();
}
