/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import com.sk89q.intake.parametric.annotation.Validate;
import li.l1t.common.intake.provider.annotation.Merged;
import me.minotopia.expvp.command.service.SkillCommandService;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.command.CommandSender;

import java.io.IOException;

/**
 * A command that provides tools for creation, deletion and modification of skill metadata.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class CommandSkillAdmin {
    @Command(aliases = "new", min = 2,
            desc = "Erstellt neuen Skill.",
            help = "Erstellt einen neuen Skill\nzur Verwendung in Skilltrees.\nDie Id besteht " +
                    "dabei aus\nZahlen, Buchstaben und Bindestrichen\nund ist eindeutig.",
            usage = "[id] [name...]")
    @Require("expvp.admin")
    public void newSkill(SkillCommandService service, CommandSender sender,
                         @Validate(regex = "[a-zA-Z0-9\\-]+") String id,
                         @Merged(translateColors = true)  String name)
            throws IOException {
        Skill skill = service.createSkillWithExistsCheck(id);
        skill.setName(name);
        service.saveSkill(skill);
        sender.sendMessage(String.format(
                "§e➩ Neuer Skill mit der ID '%s' und dem Namen '%s' erstellt.",
                id, name));
    }

    @Command(aliases = "setname", min = 2,
            desc = "Ändert den Namen eines Skills.",
            usage = "[id] [name...]")
    @Require("expvp.admin")
    public void editName(SkillCommandService service, CommandSender sender,
                         Skill skill,
                         @Merged(translateColors = true) String name)
            throws IOException {
        String previousName = skill.getName();
        skill.setName(name);
        service.saveSkill(skill);
        sender.sendMessage(String.format(
                "§e➩ Name des Skills '%s' von '%s' auf '%s' geändert.",
                skill.getId(), previousName, name));
    }
}
