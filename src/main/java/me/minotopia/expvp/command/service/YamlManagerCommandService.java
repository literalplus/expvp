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

import li.l1t.common.i18n.Message;
import li.l1t.common.intake.CommandsManager;
import me.minotopia.expvp.api.Identifiable;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import me.minotopia.expvp.util.SessionProvider;
import me.minotopia.expvp.yaml.YamlManager;
import org.bukkit.command.CommandSender;

import java.io.IOException;

/**
 * Provides base utilities for working with object registries managed by {@link YamlManager}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-13
 */
public class YamlManagerCommandService<T extends Identifiable> extends CommandService {
    protected final YamlManager<T> manager;
    private Message objectType;

    YamlManagerCommandService(SessionProvider sessionProvider, PlayerDataService playerDataService,
                              YamlManager<T> manager, String objectTypeKey, CommandsManager commandsManager) {
        super(sessionProvider, playerDataService, commandsManager);
        this.manager = manager;
        this.objectType = Message.of(objectTypeKey);
    }

    public void saveObject(T object) {
        try {
            manager.save(object);
        } catch (IOException e) {
            e.printStackTrace();
            throw new I18nUserException("error!reg.error-saving", objectType, object.getId());
        }
    }

    public T createObjectWithExistsCheck(String id) throws IOException {
        assureThereIsNoObjectWithId(id);
        return manager.create(id);
    }

    private void assureThereIsNoObjectWithId(String id) {
        if (manager.contains(id)) {
            throw new I18nUserException("error!reg.already-exists", objectType, id);
        }
    }

    public T getObjectOrFail(String id) {
        T object = manager.get(id);
        if (object == null) {
            throw new I18nUserException("error!reg.there-is-no", objectType, id);
        }
        return object;
    }

    public void sendChangeNotification(String descriptionKey, Object previous, Object changed,
                                       T object, CommandSender sender) {
        I18n.sendLoc(sender, Format.success("admin!reg.changed", Message.of(descriptionKey), object.getId(), previous, changed));
    }

    public YamlManager<T> getManager() {
        return manager;
    }

    public Message getObjectType() {
        return objectType;
    }
}
