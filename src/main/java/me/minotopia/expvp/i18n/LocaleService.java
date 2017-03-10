/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.i18n.MinecraftLocale;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

import java.util.Locale;

/**
 * Manages the locale preference of players from the database and receives notifications about changed client settings.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-07
 */
@Singleton
public class LocaleService {
    private final PlayerDataService players;
    private final SessionProvider sessionProvider;

    @Inject
    public LocaleService(PlayerDataService players, SessionProvider sessionProvider) {
        this.players = players;
        this.sessionProvider = sessionProvider;
    }

    public void enable(EPPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(plugin.inject(LocaleJoinLeaveListener.class), plugin);
        ProtocolLibrary.getProtocolManager().addPacketListener(plugin.inject(LocaleChangeListener.class));
        plugin.async(() -> {
            try (ScopedSession scoped = sessionProvider.scoped().join()) {
                plugin.getServer().getOnlinePlayers().forEach(this::recomputeClientLocale);
                scoped.commitIfLastAndChanged();
            }
        });
    }

    /**
     * Updates given player's locale preference in the database if they have not explicitly chosen another locale.
     *
     * @param player the player to operate on
     * @return the computed preferred locale of given player
     */
    public Locale recomputeClientLocale(Player player) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            MutablePlayerData playerData = players.findOrCreateDataMutable(player.getUniqueId());
            Locale locale;
            if (!playerData.hasCustomLocale()) {
                locale = MinecraftLocale.toJava(player.spigot().getLocale());
                setLocaleIfDifferent(playerData, locale);
            } else {
                locale = playerData.getLocale();
            }
            scoped.commitIfLastAndChanged();
            I18n.setLocaleFor(player.getUniqueId(), locale);
            return locale;
        }
    }

    private void setLocaleIfDifferent(MutablePlayerData playerData, Locale newLocale) {
        Locale oldLocale = playerData.getLocale();
        if (!newLocale.equals(oldLocale)) {
            playerData.setLocale(newLocale);
            players.saveData(playerData);
        }
    }
}
