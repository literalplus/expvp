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

package me.minotopia.expvp.api.handler.damage;

import me.minotopia.expvp.api.handler.SkillHandler;
import org.bukkit.entity.Player;

/**
 * A handler that gets called for damage events.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public interface DamageHandler extends SkillHandler {
    /**
     * Handles a player who has obtained the skill handled by this handler being hit by another player.
     *
     * @param victim  the player who was hit and has obtained this skill
     * @param culprit the player who dealt the hit
     */
    void handleVictim(Player victim, Player culprit);

    /**
     * Handles a player who has obtained the skill handled by this handler hitting another player.
     *
     * @param culprit the player who dealt the hit and has obtained this skill
     * @param victim  the player who was hit
     */
    void handleCulprit(Player culprit, Player victim);
}
