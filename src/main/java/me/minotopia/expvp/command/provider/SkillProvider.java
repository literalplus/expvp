/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.provider;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import me.minotopia.expvp.command.service.SkillCommandService;
import me.minotopia.expvp.skill.meta.Skill;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides skill instances to commands.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-06
 */
public class SkillProvider implements Provider<Skill> {
    private final SkillCommandService service;

    public SkillProvider(SkillCommandService service) {
        this.service = service;
    }

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public Skill get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
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
