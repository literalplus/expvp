/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Validate;
import li.l1t.common.intake.provider.annotation.Colored;
import li.l1t.common.intake.provider.annotation.ItemInHand;
import li.l1t.common.intake.provider.annotation.Merged;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.command.permission.EnumRequires;
import me.minotopia.expvp.command.service.SkillCommandService;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

/**
 * A command that provides tools for creation, deletion and modification of skill metadata.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class CommandSkillAdmin extends YamlManagerCommandBase<Skill> {
    @Override
    public String getObjectTypeName() {
        return "Skill";
    }

    @Command(aliases = "new", min = 2,
            desc = "Erstellt neuen Skill",
            help = "Erstellt einen neuen Skill\nzur Verwendung in Skilltrees.\nDie Id besteht " +
                    "dabei aus\nZahlen, Buchstaben und Bindestrichen\nund ist eindeutig.",
            usage = "[id] [name...]")
    @EnumRequires(Permission.ADMIN_SKILL)
    public void newSkill(SkillCommandService service, CommandSender sender,
                         @Validate(regex = "[a-zA-Z0-9\\-]+") String id,
                         @Merged @Colored String name)
            throws IOException {
        createNew(service, sender, id, name);
    }

    @Command(aliases = "name", min = 2,
            desc = "Ändert den Namen",
            usage = "[id] [name...]")
    @EnumRequires(Permission.ADMIN_SKILL)
    public void editName(SkillCommandService service, CommandSender sender,
                         Skill skill,
                         @Merged @Colored String name)
            throws IOException {
        service.changeName(skill, name, sender);
    }

    @Command(aliases = "handler", min = 2,
            desc = "Ändert die Aktion",
            help = "Aktionen werden so spezifiziert:\ntyp/argument\nMehr Info: /ska handlers",
            usage = "[id] [handler...]")
    @EnumRequires(Permission.ADMIN_SKILL)
    public void editHandler(SkillCommandService service, CommandSender sender,
                         Skill skill,
                         @Merged String handlerSpec)
            throws IOException {
        service.changeHandlerSpec(skill, handlerSpec, sender);
    }

    @Command(aliases = "icon", min = 2,
            desc = "Ändert das Icon",
            help = "Ändert das Icon auf das\nItem in deiner Hand",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_SKILL)
    public void editIcon(SkillCommandService service, CommandSender sender,
                         Skill skill, @ItemInHand ItemStack itemInHand)
            throws IOException {
        service.changeIconStack(skill, itemInHand, sender);
    }

    @Command(aliases = "cost", min = 2,
            desc = "Ändert den Preis",
            help = "Ändert den Preis\nPreis in Skillpunkten.",
            usage = "[id] [neuer Preis]")
    @EnumRequires(Permission.ADMIN_SKILL)
    public void editCost(SkillCommandService service, CommandSender sender,
                         Skill skill, int bookCost)
            throws IOException {
        service.changeBookCost(skill, bookCost, sender);
    }

    @Command(aliases = "info", min = 1,
            desc = "Zeigt Infos zu einem Skill",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_BASIC)
    public void skillInfo(SkillCommandService service, CommandSender sender,
                         Skill skill)
            throws IOException {
        sender.sendMessage(String.format("§e➩ §lSkill: §6%s §e(ID: %s§e)",
                skill.getDisplayName(), skill.getId()));
        sender.sendMessage(String.format("§e➩ §lHandler (Aktion): §6%s",
                skill.getHandlerSpec()));
        sender.sendMessage(String.format("§e➩ §lPreis in Skillpunkten: §6%s",
                skill.getBookCost()));
        sender.sendMessage(String.format("§e➩ §lIcon: %s",
                skill.getIconStack() == null ? "§cnein" : skill.getIconStack()));
    }

    //TODO: /ska handlers
}