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
import me.minotopia.expvp.i18n.exception.I18nUserException;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

/**
 * Provides LocalDate instances from arguments.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-17
 */
public class LocalDateProvider implements Provider<LocalDate> {
    @Override
    public boolean isProvided() {
        return false;
    }

    @Override
    public LocalDate get(CommandArgs args, List<? extends Annotation> annotations) throws ArgumentException, ProvisionException {
        String input = args.next();
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            throw new I18nUserException("error!invalid-temporal", input, e.getMessage());
        }
    }

    @Override
    public List<String> getSuggestions(String s) {
        return Collections.emptyList();
    }
}
