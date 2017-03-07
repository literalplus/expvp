/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.minotopia.expvp.EPPlugin;

/**
 * Listens for locale changes and forwards them to the Internationalisation subsystem.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-07
 */
public class LocaleChangeListener extends PacketAdapter {
    private final EPPlugin plugin;
    private final LocaleService localeService;

    public LocaleChangeListener(EPPlugin plugin, LocaleService localeService) {
        super(plugin, PacketType.Play.Client.SETTINGS);
        this.plugin = plugin;
        this.localeService = localeService;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        plugin.async(() -> localeService.recomputeClientLocale(event.getPlayer()));
    }
}
