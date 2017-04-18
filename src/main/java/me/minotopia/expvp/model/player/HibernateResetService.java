/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.player;

import com.google.inject.Inject;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.misc.ConstructOnEnable;
import me.minotopia.expvp.util.SessionProvider;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Resets specific temporary stats for all players.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-18
 */
@ConstructOnEnable
public class HibernateResetService {
    private final SessionProvider sessionProvider;

    @Inject
    public HibernateResetService(SessionProvider sessionProvider, TaskService tasks) {
        this.sessionProvider = sessionProvider;
        tasks.weeklyAt(this::resetAllTemporaryStats, DayOfWeek.FRIDAY, LocalTime.MIDNIGHT);
    }

    @SuppressWarnings("JpaQlInspection")
    public void resetAllTemporaryStats() {
        sessionProvider.inSession(session -> {
            session.tx();
            session.session().createQuery(
                    "update HibernatePlayerData set " +
                            "currentKills = 0, " +
                            "currentDeaths = 0, " +
                            "talentPoints = 0"
            ).executeUpdate();
            session.session().createQuery(
                    "delete from HibernateObtainableSkill"
            ).executeUpdate();
            session.commit();
        });
    }
}
