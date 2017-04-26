/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
