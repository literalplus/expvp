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
import li.l1t.common.intake.provider.annotation.Colored;
import li.l1t.common.intake.provider.annotation.ItemInHand;
import li.l1t.common.intake.provider.annotation.Merged;
import me.minotopia.expvp.command.service.SkillCommandService;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

/**
 * A command that provides tools for creation, deletion and modification of skill trees.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class CommandSkillTreeAdmin {
    @Command(aliases = "new", min = 2,
            desc = "Erstellt neuen Skilltree",
            help = "Erstellt einen neuen Skilltree\nDie Id besteht " +
                    "dabei aus\nZahlen, Buchstaben und Bindestrichen\nund ist eindeutig.",
            usage = "[id] [name...]")
    @Require("expvp.admin")
    public void newSkill(SkillCommandService service, CommandSender sender,
                         @Validate(regex = "[a-zA-Z0-9\\-]+") String id,
                         @Merged @Colored String name)
            throws IOException {
        Skill skill = service.createObjectWithExistsCheck(id);
        skill.setName(name);
        service.saveObject(skill);
        sender.sendMessage(String.format(
                "§e➩ Neuer Skill mit der ID '%s' und dem Namen '%s' erstellt.",
                id, name));
    }

    @Command(aliases = "name", min = 2,
            desc = "Ändert den Namen",
            usage = "[id] [name...]")
    @Require("expvp.admin")
    public void editName(SkillCommandService service, CommandSender sender,
                         Skill skill,
                         @Merged @Colored String name)
            throws IOException {
        String previousName = skill.getName();
        skill.setName(name);
        service.saveObject(skill);
        sendChangeNotification("Name", previousName, name, skill, sender);
    }

    @Command(aliases = "handler", min = 2,
            desc = "Ändert die Aktion",
            help = "Aktionen werden so spezifiziert:\ntyp/argument\nMehr Info: /ska handlers",
            usage = "[id] [handler...]")
    @Require("expvp.admin")
    public void editHandler(SkillCommandService service, CommandSender sender,
                         Skill skill,
                         @Merged String name)
            throws IOException {
        String previousName = skill.getHandlerSpec();
        skill.setHandlerSpec(name);
        service.saveObject(skill);
        sendChangeNotification("Handler", previousName, name, skill, sender);
    }

    @Command(aliases = "icon", min = 2,
            desc = "Ändert das Icon",
            help = "Ändert das Icon auf das\nItem in deiner Hand",
            usage = "[id]")
    @Require("expvp.admin")
    public void editIcon(SkillCommandService service, CommandSender sender,
                         Skill skill, @ItemInHand ItemStack itemInHand)
            throws IOException {
        ItemStack previousStack = skill.getIconStack();
        skill.setIconStack(itemInHand);
        service.saveObject(skill);
        sendChangeNotification("Icon", previousStack, itemInHand, skill, sender);
    }

    @Command(aliases = "cost", min = 2,
            desc = "Ändert den Preis",
            help = "Ändert den Preis\nPreis in Skillpunkten.",
            usage = "[id]")
    @Require("expvp.admin")
    public void editCost(SkillCommandService service, CommandSender sender,
                         Skill skill, int bookCost)
            throws IOException {
        int previousCost = skill.getBookCost();
        skill.setBookCost(bookCost);
        service.saveObject(skill);
        sendChangeNotification("Preis in Skillpunkten", previousCost, bookCost, skill, sender);
    }

    private void sendChangeNotification(String description, Object previous, Object changed, Skill
            skill, CommandSender sender) {
        sender.sendMessage(String.format(
                "§e➩ " + description + " des Skills '%s' von '%s' auf '%s' geändert.",
                skill.getId(), previous, changed));
    }

    @Command(aliases = "info", min = 1,
            desc = "Zeigt Infos zu einem Skill",
            usage = "[id]")
    @Require("expvp.admin")
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
