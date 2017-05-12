/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
        });
        sessionProvider.inSession(session -> session.session().getSessionFactory().getCache().evictAllRegions());
        LOGGER.debug("Finished resetting temporary stats.");
    }
}
