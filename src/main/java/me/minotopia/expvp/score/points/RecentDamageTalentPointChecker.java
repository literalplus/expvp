/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
        server.getOnlinePlayers().forEach(player -> {
            talentPoints.grantDeservedTalentPoints(player, TalentPointType.RECENT_DAMAGE);
        });
    }
}
