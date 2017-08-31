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

package me.minotopia.expvp.command.permission;

import com.sk89q.intake.CommandException;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.ArgumentParser;
import com.sk89q.intake.util.auth.AuthorizationException;
import li.l1t.common.intake.exception.UncheckedException;
import li.l1t.common.intake.util.InvokeAdapter;
import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Checks command invocations for {@link EnumRequires} annotations and the corresponding
 * permissions.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-15
 */
public class EnumPermissionInvokeListener extends InvokeAdapter {
    @Override
    public boolean preInvoke(
            List<? extends Annotation> annotations, ArgumentParser parser, Object[] args, CommandArgs commandArgs
    ) throws CommandException, ArgumentException {
        Optional<? extends EnumRequires> possibleEnumRequires = enumRequiresAnnotationIn(annotations);
        if (possibleEnumRequires.isPresent()) {
            CommandSender sender = commandArgs.getNamespace().get(CommandSender.class);
            if (sender == null) {
                throw new CommandException("Cannot check permission without command sender!");
            }
            checkHasAllRequiredPermissions(possibleEnumRequires.get(), sender);
        }
        return true;
    }

    private void checkHasAllRequiredPermissions(EnumRequires annotation, CommandSender sender) {
        if (!hasAllPermissionsIn(sender, annotation)) {
            throw UncheckedException.wrap(new AuthorizationException());
        }
    }

    private boolean hasAllPermissionsIn(CommandSender sender, EnumRequires annotation) {
        return Arrays.stream(annotation.value())
                .allMatch(perm -> perm.has(sender));
    }

    private Optional<? extends EnumRequires> enumRequiresAnnotationIn(List<? extends Annotation> annotations) {
        return annotations.stream()
                .filter(ann -> ann instanceof EnumRequires)
                .map(ann -> (EnumRequires) ann)
                .findFirst();
    }
}
