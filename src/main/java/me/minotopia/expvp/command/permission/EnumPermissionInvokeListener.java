/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.permission;

import com.sk89q.intake.CommandException;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.ArgumentParser;
import com.sk89q.intake.util.auth.AuthorizationException;
import li.l1t.common.intake.exception.UncheckedException;
import li.l1t.common.intake.util.InvokeAdapter;
import me.minotopia.expvp.Permission;
import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

/**
 * Checks command invocations for {@link EnumRequires} annotations and the corresponding
 * permissions.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-15
 */
public class EnumPermissionInvokeListener extends InvokeAdapter {
    @Override
    public boolean preInvoke(
            List<? extends Annotation> annotations, ArgumentParser parser, Object[] args, CommandArgs commandArgs
    ) throws CommandException, ArgumentException {
        Optional<? extends EnumRequires> possibleEnumRequires = enumRequiresAnnotationIn(annotations);
        if (possibleEnumRequires.isPresent()) {
            Permission permission = possibleEnumRequires.get().value();
            CommandSender sender = commandArgs.getNamespace().get(CommandSender.class);
            if (sender == null) {
                throw new CommandException("Could not check permission without command sender!");
            }
            if (!permission.has(sender)) {
                throw UncheckedException.wrap(new AuthorizationException());
            }
        }
        return true;
    }

    private Optional<? extends EnumRequires> enumRequiresAnnotationIn(List<? extends Annotation> annotations) {
        return annotations.stream()
                .filter(ann -> ann instanceof EnumRequires)
                .map(ann -> (EnumRequires) ann)
                .findFirst();
    }
}
