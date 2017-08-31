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

package me.minotopia.expvp.extimes;

import com.google.inject.Inject;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.extimes.ExTimesService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.LocaleService;
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
    private final LocaleService localeService;

    @Inject
    public ExTimesJoinListener(ExTimesService times, LocaleService localeService) {
        this.times = times;
        this.localeService = localeService;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!times.isCurrentlyOnline()) {
            if (Permission.ADMIN_EXTIMES_BYPASS.has(event.getPlayer())) {
                I18n.sendLoc(event.getPlayer(), "admin!extimes.bypass");
            } else {
                localeService.recomputeClientLocale(event.getPlayer());
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
