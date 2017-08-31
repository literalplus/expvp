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

package me.minotopia.expvp.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.LocaleService;
import org.bukkit.entity.Player;

import java.util.Locale;

/**
 * Allows players to change their language preferences.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-26
 */
@AutoRegister("lang")
public class CommandLang {
    private final LocaleService localeService;

    @Inject
    public CommandLang(LocaleService localeService) {
        this.localeService = localeService;
    }

    @Command(aliases = "", desc = "cmd!lang.root.desc")
    public void root(@Sender Player player) {
        I18n.sendLoc(player, Format.result("core!lang.current-locale"));
    }

    @Command(aliases = "de", desc = "cmd!lang.de.desc")
    public void de(@Sender Player player) {
        localeService.forceLocale(player, Locale.GERMAN);
        notifyNewLocale(player);
    }

    private void notifyNewLocale(@Sender Player player) {
        I18n.sendLoc(player, Format.success("core!lang.changed-locale"));
    }

    @Command(aliases = "en", desc = "cmd!lang.en.desc")
    public void en(@Sender Player player) {
        localeService.forceLocale(player, Locale.ENGLISH);
        notifyNewLocale(player);
    }

    @Command(aliases = "auto", desc = "cmd!lang.auto.desc")
    public void auto(@Sender Player player) {
        localeService.resetLocale(player);
        notifyNewLocale(player);
    }
}
