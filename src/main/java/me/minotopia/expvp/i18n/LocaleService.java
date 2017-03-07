/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

import li.l1t.common.i18n.MinecraftLocale;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Optional;

/**
 * Manages the locale preference of players from the database and receives notifications about changed client settings.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-07
 */
public class LocaleService {
    private final PlayerDataService players;
    private final SessionProvider sessionProvider;

    public LocaleService(PlayerDataService players, SessionProvider sessionProvider) {
        this.players = players;
        this.sessionProvider = sessionProvider;
    }

    /**
     * Finds the preferred locale of given player, first looking for their explicitly preferred locale in the database,
     * and if that's not set, uses the client locale of the player.
     *
     * @param player the player to operate on
     * @return the preferred locale of given player
     */
    public Locale findPlayerLocale(Player player) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            Optional<? extends MutablePlayerData> data = players.findDataMutable(player.getUniqueId());
            if (data.isPresent()) {
                Locale locale = findLocale(player, data.get());
                scoped.commitIfLastAndChanged();
                return locale;
            } else {
                return MinecraftLocale.toJava(player.spigot().getLocale());
            }
        }
    }

    private Locale findLocale(Player player, MutablePlayerData playerData) {
        if (playerData.hasCustomLocale()) {
            return playerData.getLocale();
        } else {
            Locale clientLocale = MinecraftLocale.toJava(player.spigot().getLocale());
            setLocaleIfDifferent(playerData, clientLocale);
            return clientLocale;
        }
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
