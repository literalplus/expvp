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
import com.sk89q.intake.parametric.annotation.Text;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skill.meta.SkillManager;
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
            desc = "Erstellt einen neuen Skill zur Verwendung in Skilltrees.")
    @Require("expvp.admin")
    public void newSkill(SkillManager skillManager, CommandSender sender,
                         String id, @Text String name) throws
            IOException {
        if (skillManager.contains(id)) {
            sender.sendMessage(String.format(
                    "§c§lFehler: §cEs gibt bereits einen Skill mit der ID '%s'",
                    id));
            return;
        }
        Skill skill = skillManager.create(id);
        skill.setName(name);
        sender.sendMessage(String.format(
                "§e➩ Neuer Skill mit der ID '%s' und dem Namen '%s' erstellt.",
                id, name));
    }
}
