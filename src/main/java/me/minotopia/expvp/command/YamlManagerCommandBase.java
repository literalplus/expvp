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

import me.minotopia.expvp.api.Identifiable;
import me.minotopia.expvp.command.service.YamlManagerCommandService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.command.CommandSender;

import java.io.IOException;

/**
 * Base class for commands related to YamlManager.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-13
 */
abstract class YamlManagerCommandBase<T extends Identifiable, S extends YamlManagerCommandService<T>>
        extends AbstractServiceBackedCommand<S> {
    protected YamlManagerCommandBase(S commandService) {
        super(commandService);
    }

    public abstract String getObjectTypeName();

    T createNew(CommandSender sender,
                String id) throws IOException {
        T object = service().createObjectWithExistsCheck(id);
        I18n.sendLoc(sender, Format.success("admin!reg.created", service().getObjectType(), id));
        return object;
    }
}
