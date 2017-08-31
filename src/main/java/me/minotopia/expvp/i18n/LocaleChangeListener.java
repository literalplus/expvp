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

package me.minotopia.expvp.i18n;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.inject.Inject;
import me.minotopia.expvp.EPPlugin;
import org.bukkit.entity.Player;

/**
 * Listens for locale changes and forwards them to the Internationalisation subsystem.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-07
 */
public class LocaleChangeListener extends PacketAdapter {
    private final EPPlugin plugin;
    private final LocaleService localeService;

    @Inject
    LocaleChangeListener(EPPlugin plugin, LocaleService localeService) {
        super(plugin, PacketType.Play.Client.SETTINGS);
        this.plugin = plugin;
        this.localeService = localeService;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        plugin.async(() -> localeService.recomputeClientLocale(player));
    }
}
