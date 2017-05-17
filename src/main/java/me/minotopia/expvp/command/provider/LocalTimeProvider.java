/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.provider;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import me.minotopia.expvp.i18n.exception.I18nUserException;

import java.lang.annotation.Annotation;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

/**
 * Provides LocalTime instances from arguments.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-17
 */
public class LocalTimeProvider implements Provider<LocalTime> {
    @Override
    public boolean isProvided() {
        return false;
    }

    @Override
    public LocalTime get(CommandArgs args, List<? extends Annotation> annotations) throws ArgumentException, ProvisionException {
        String input = args.next();
        try {
            return LocalTime.parse(input);
        } catch (DateTimeParseException e) {
            throw new I18nUserException("error!invalid-temporal", input, e.getMessage());
        }
    }

    @Override
    public List<String> getSuggestions(String s) {
        return Collections.emptyList();
    }
}
