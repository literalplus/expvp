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

package me.minotopia.expvp.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Validate;
import li.l1t.common.intake.provider.annotation.ItemInHand;
import li.l1t.common.intake.provider.annotation.Merged;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.command.permission.EnumRequires;
import me.minotopia.expvp.command.service.SkillCommandService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.ui.menu.SelectSkillMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

/**
 * A command that provides tools for creation, deletion and modification of skill metadata.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-07-23
 */
@AutoRegister("ska")
public class CommandSkillAdmin extends YamlManagerCommandBase<Skill, SkillCommandService> {
    private final DisplayNameService names;

    @Inject
    protected CommandSkillAdmin(SkillCommandService commandService, DisplayNameService names) {
        super(commandService);
        this.names = names;
    }

    @Override
    public String getObjectTypeName() {
        return "Skill";
    }

    @Command(aliases = "new", min = 1,
            desc = "Erstellt neuen Skill",
            help = "Erstellt einen neuen Skill\nzur Verwendung in Skilltrees.\nDie Id besteht " +
                    "dabei aus\nZahlen, Buchstaben und Bindestrichen\nund ist eindeutig.",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_SKILL)
    public void newSkill(CommandSender sender, @Validate(regex = "[a-zA-Z0-9\\-]+") String id)
            throws IOException {
        service().saveObject(createNew(sender, id));
    }

    @Command(aliases = "handler", min = 2,
            desc = "Ändert die Aktion",
            help = "Aktionen werden so spezifiziert:\ntyp/argument\nMehr Info: /ska handlers",
            usage = "[id] [handler...]")
    @EnumRequires(Permission.ADMIN_SKILL)
    public void editHandler(CommandSender sender, Skill skill, @Merged String handlerSpec)
            throws IOException {
        service().changeHandlerSpec(skill, handlerSpec, sender);
    }

    @Command(aliases = "icon", min = 2,
            desc = "Ändert das Icon",
            help = "Ändert das Icon auf das\nItem in deiner Hand",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_SKILL)
    public void editIcon(CommandSender sender, Skill skill, @ItemInHand ItemStack itemInHand)
            throws IOException {
        service().changeIconStack(skill, itemInHand, sender);
    }

    @Command(aliases = "cost", min = 2,
            desc = "Ändert den Preis",
            help = "Ändert den Preis\nPreis in Skillpunkten.",
            usage = "[id] [neuer Preis]")
    @EnumRequires(Permission.ADMIN_SKILL)
    public void editCost(CommandSender sender, Skill skill, int bookCost)
            throws IOException {
        service().changeBookCost(skill, bookCost, sender);
    }

    @Command(aliases = "info", min = 1,
            desc = "Zeigt Infos zu einem Skill",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_BASIC)
    public void skillInfo(CommandSender sender, Skill skill) {
        I18n.sendLoc(sender, Format.header("admin!skill.info.header", skill.getId()));
        I18n.sendLoc(sender, Format.result("admin!skill.info.name", names.displayName(skill)));
        I18n.sendLoc(sender, Format.result("admin!skill.info.desc", names.description(skill)));
        I18n.sendLoc(sender, Format.result("admin!skill.info.handler", skill.getHandlerSpec()));
        I18n.sendLoc(sender, Format.result("admin!skill.info.misc",
                skill.getTalentPointCost(), Format.bool(skill.getIconStack() != null)));
        I18n.sendLoc(sender, Format.result("admin!skill.info.lang-files"));
    }

    @Command(aliases = "list",
            desc = "Zeigt alle Skills",
            help = "Zeigt alle Skills\nin einem Inventar.")
    @EnumRequires(Permission.ADMIN_SKILL)
    public void list(SelectSkillMenu.Factory selectSkillMenuFactory, @Sender Player player) {
        selectSkillMenuFactory.openMenu(
                player, skill -> {
                    skillInfo(player, skill);
                    player.closeInventory();

                });
    }

    @Command(aliases = "remove", min = 1,
            desc = "Löscht einen Skill",
            usage = "[id]")
    @EnumRequires({Permission.ADMIN_SKILL, Permission.ADMIN_OVERRIDE})
    public void removeSkill(CommandSender sender, Skill skill)
            throws IOException {
        service().getManager().remove(skill);
    }
}
