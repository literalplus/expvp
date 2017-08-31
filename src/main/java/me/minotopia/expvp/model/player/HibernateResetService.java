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

package me.minotopia.expvp.model.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.logging.LoggingManager;
import me.minotopia.expvp.util.SessionProvider;
import org.apache.logging.log4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Resets specific temporary stats for all players.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-18
 */
@Singleton
@ConstructOnEnable
public class HibernateResetService {
    private static final Logger LOGGER = LoggingManager.getLogger(HibernateResetService.class);
    private final SessionProvider sessionProvider;

    @Inject
    public HibernateResetService(SessionProvider sessionProvider, TaskService tasks) {
        this.sessionProvider = sessionProvider;
        tasks.weeklyAt(this::resetAllTemporaryStats, DayOfWeek.FRIDAY, LocalTime.MIDNIGHT);
    }

    @SuppressWarnings("JpaQlInspection")
    public void resetAllTemporaryStats() {
        LOGGER.info("Now resetting all temporary stats...");
        sessionProvider.inSession(scoped -> {
            scoped.tx();
            scoped.session().createQuery(
                    "update HibernatePlayerData set " +
                            "currentKills = 0, " +
                            "currentDeaths = 0, " +
                            "talentPoints = 0"
            ).executeUpdate();
            scoped.session().createQuery(
                    "delete from HibernateObtainedSkill"
            ).executeUpdate();
            scoped.session().createQuery(
                    "delete from HibernatePlayerPoints"
            ).executeUpdate();
        });
        sessionProvider.inSession(session -> session.session().getSessionFactory().getCache().evictAllRegions());
        LOGGER.debug("Finished resetting temporary stats.");
    }
}
