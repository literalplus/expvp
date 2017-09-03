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

import com.google.inject.Inject;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.api.score.points.TalentPointService;
import me.minotopia.expvp.api.score.points.TalentPointType;
import org.bukkit.Server;

import java.time.Duration;

/**
 * Periodically checks for recent damage relevant to Talent Point granting.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-16
 */
@ConstructOnEnable
public class RecentDamageTalentPointChecker {
    private final Server server;
    private final TalentPointService talentPoints;

    @Inject
    public RecentDamageTalentPointChecker(TaskService tasks, Server server, TalentPointService talentPoints) {
        this.server = server;
        this.talentPoints = talentPoints;
        tasks.repeating(this::checkAllOnlinePlayers, Duration.ofMinutes(5));
    }

    private void checkAllOnlinePlayers() {
        server.getOnlinePlayers().forEach(player ->
                talentPoints.grantDeservedTalentPoints(player, TalentPointType.RECENT_DAMAGE)
        );
    }
}