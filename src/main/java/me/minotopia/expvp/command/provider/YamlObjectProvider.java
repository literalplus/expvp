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

package me.minotopia.expvp.command.provider;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import me.minotopia.expvp.api.Identifiable;
import me.minotopia.expvp.command.service.YamlManagerCommandService;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides skill tree instances to commands.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-06
 */
public class YamlObjectProvider<T extends Identifiable> implements Provider<T> {
    private final YamlManagerCommandService<T> service;

    public YamlObjectProvider(YamlManagerCommandService<T> service) {
        this.service = service;
    }

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public T get(CommandArgs arguments, List<? extends Annotation> modifiers) throws
            ArgumentException, ProvisionException {
        String skillId = arguments.next();
        return service.getObjectOrFail(skillId);
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return service.getManager().getAll().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }
}
