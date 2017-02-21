/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.service;

import li.l1t.common.exception.InternalException;
import li.l1t.common.exception.UserException;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.shared.uuid.UUIDRepositories;
import li.l1t.common.shared.uuid.UUIDRepository;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.util.ScopedSession;
import me.minotopia.expvp.util.SessionProvider;
import org.hibernate.HibernateException;

import java.util.UUID;
import java.util.function.Function;

/**
 * Base implementation of a command service with some common methods.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-14
 */
public class CommandService {
    private static boolean registeredPlainService = false;
    private final SessionProvider sessionProvider;
    private final PlayerDataService playerDataService;

    public CommandService(SessionProvider sessionProvider, PlayerDataService playerDataService) {
        this.sessionProvider = sessionProvider;
        this.playerDataService = playerDataService;
    }

    public CommandService(EPPlugin plugin) {
        this(plugin.getSessionProvider(), plugin.getPlayerDataService());
    }

    public void registerInjections(CommandsManager commandsManager) {
        if (!registeredPlainService) {
            commandsManager.bind(CommandService.class).toInstance(this);
            registeredPlainService = true;
        }
    }

    public PlayerData findPlayerData(UUID playerId) {
        return modifyPlayerData(playerId, playerData -> playerData);
    }

    public <T> T modifyPlayerData(UUID playerId, Function<MutablePlayerData, T> mutator) {
        try (ScopedSession scoped = sessionProvider.scoped().join()) {
            MutablePlayerData playerData = playerDataService.findOrCreateDataMutable(playerId);
            T result = mutator.apply(playerData);
            playerDataService.saveData(playerData);
            scoped.commitIfLast();
            return result;
        } catch (HibernateException e) {
            throw new InternalException("Datenbankfehler", e);
        }
    }

    public UUID findPlayerByNameOrIdOrFail(String playerInput) {
        try {
            return UUIDRepositories.getUUIDChecked(playerInput);
        } catch (UUIDRepository.UnknownKeyException e) {
            throw new UserException("Unbekannter Spieler: " + playerInput);
        } catch (UUIDRepository.InvalidResultException e) {
            throw new UserException("Zu diesem Namen gibt es mehrere Spieler. Probiere die UUID.");
        }
    }
}
