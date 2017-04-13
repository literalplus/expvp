/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.service;

import com.google.inject.Inject;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.shared.uuid.UUIDRepositories;
import li.l1t.common.shared.uuid.UUIDRepository;
import me.minotopia.expvp.api.exception.UnknownPlayerException;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import me.minotopia.expvp.util.SessionProvider;

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
    private final PlayerDataService players;

    @Inject
    public CommandService(SessionProvider sessionProvider, PlayerDataService players,
                          CommandsManager commandsManager) {
        this.sessionProvider = sessionProvider;
        this.players = players;
        registerInjections(commandsManager);
    }

    protected void registerInjections(CommandsManager commandsManager) {
        if (!registeredPlainService) {
            commandsManager.bind(CommandService.class).toInstance(this);
            registeredPlainService = true;
        }
    }

    public PlayerData findPlayerData(UUID playerId) {
        return modifyPlayerData(playerId, playerData -> playerData);
    }

    public <T> T modifyPlayerData(UUID playerId, Function<MutablePlayerData, T> mutator) {
        return sessionProvider.inSessionAnd(ignored -> {
            MutablePlayerData playerData = players.findOrCreateDataMutable(playerId);
            T result = mutator.apply(playerData);
            players.saveData(playerData);
            return result;
        });
    }

    public UUID findPlayerByNameOrIdOrFail(String playerInput) {
        try {
            return UUIDRepositories.getUUIDChecked(playerInput);
        } catch (UUIDRepository.UnknownKeyException e) {
            throw new UnknownPlayerException(playerInput);
        } catch (UUIDRepository.InvalidResultException e) {
            throw new I18nUserException("error!player.multiple");
        }
    }
}
