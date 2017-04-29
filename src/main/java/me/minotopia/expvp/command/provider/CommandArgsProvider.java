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

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

/**
 * Provides CommandArgs instances to commands.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-29
 */
/* why do we even need this */
public class CommandArgsProvider implements Provider<CommandArgs> {
    @Override
    public boolean isProvided() {
        return true;
    }

    @Nullable
    @Override
    public CommandArgs get(CommandArgs arguments, List<? extends Annotation> modifiers) throws
            ArgumentException, ProvisionException {
        return arguments;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return Collections.emptyList();
    }
}
