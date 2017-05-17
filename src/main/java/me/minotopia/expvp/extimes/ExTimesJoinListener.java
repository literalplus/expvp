/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.extimes;

import com.google.inject.Inject;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.extimes.ExTimesService;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Listens for join events and blocks them for players who are not permitted to bypass ExTimes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-16
 */
public class ExTimesJoinListener implements Listener {
    private final ExTimesService times;

    @Inject
    public ExTimesJoinListener(ExTimesService times) {
        this.times = times;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!times.isCurrentlyOnline()) {
            if (Permission.ADMIN_EXTIMES_BYPASS.has(event.getPlayer())) {
                I18n.sendLoc(event.getPlayer(), "admin!extimes.bypass");
            } else {
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(I18n.loc(event.getPlayer(), findKickMessageWithNextOnlineTime()));
            }
        }
    }

    private Message findKickMessageWithNextOnlineTime() {
        return times.findNextOnlineTime()
                .map(this::findTodayOrTomorrowKickMessage)
                .orElseGet(() -> Message.of("core!extimes.kick-unknown"));
    }

    private Message findTodayOrTomorrowKickMessage(LocalDateTime nextTime) {
        if (isToday(nextTime)) {
            return Message.of("core!extimes.kick-today", formatTime(nextTime));
        } else {
            return Message.of("core!extimes.kick-tomorrow", formatTime(nextTime));
        }
    }

    private boolean isToday(LocalDateTime time) {
        return LocalDate.now().equals(time.toLocalDate());
    }

    private String formatTime(LocalDateTime time) {
        return time.toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    }
}
