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
