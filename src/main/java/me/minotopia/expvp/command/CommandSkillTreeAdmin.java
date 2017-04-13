/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Validate;
import li.l1t.common.chat.ComponentSender;
import li.l1t.common.chat.XyComponentBuilder;
import li.l1t.common.intake.i18n.Message;
import li.l1t.common.intake.provider.annotation.ItemInHand;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.command.permission.EnumRequires;
import me.minotopia.expvp.command.service.SkillTreeCommandService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.skilltree.SkillTree;
import me.minotopia.expvp.ui.menu.EditNodeMenu;
import me.minotopia.expvp.ui.menu.SelectTreeMenu;
import me.minotopia.expvp.ui.renderer.exception.RenderingException;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

/**
 * A command that provides tools for creation, deletion and modification of skill trees.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-07-23
 */
public class CommandSkillTreeAdmin extends YamlManagerCommandBase<SkillTree, SkillTreeCommandService> {
    private final DisplayNameService names;

    @Inject
    CommandSkillTreeAdmin(SkillTreeCommandService commandService, DisplayNameService names) {
        super(commandService);
        this.names = names;
    }

    @Override
    public String getObjectTypeName() {
        return "Skilltree";
    }

    @Command(aliases = "new", min = 1,
            desc = "Erstellt neuen Skilltree",
            help = "Erstellt einen neuen Skilltree\nDie Id besteht " +
                    "dabei aus\nZahlen, Buchstaben und Bindestrichen\nund ist eindeutig.",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_TREE)
    public void newSkill(CommandSender sender, @Validate(regex = "[a-zA-Z0-9\\-]+") String id)
            throws IOException {
        createNew(sender, id);
    }

    @Command(aliases = "icon", min = 1,
            desc = "Ändert das Icon",
            help = "Ändert das Icon auf das\nItem in deiner Hand",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_TREE)
    public void editIcon(CommandSender sender, SkillTree tree, @ItemInHand ItemStack itemInHand)
            throws IOException {
        service().changeIconStack(tree, itemInHand, sender);
    }

    @Command(aliases = "iconslot", min = 2,
            desc = "Ändert den Iconslot",
            help = "Ändert den Slot, in dem\ndas Icon dieses Trees\nim Übersichtsinventar ist.",
            usage = "[id] [Slot-ID]")
    @EnumRequires(Permission.ADMIN_TREE)
    public void editSlotId(CommandSender sender, SkillTree tree, int slotId)
            throws IOException {
        service().changeSlotId(tree, slotId, sender);
    }

    @Command(aliases = {"be", "branches-exclusive"}, min = 2,
            desc = "§e",
            help = "Setzt, ob sich die Äste\ndes Baums gegenseitig\nausschließen.\nDas bedeutet, " +
                    "wenn true,\nund man einen Ast erforscht hat,\nkann man den Nachbarn\nnicht " +
                    "mehr erforschen.",
            usage = "[id] [true|false]")
    @EnumRequires(Permission.ADMIN_TREE)
    public void editBranchesExclusive(CommandSender sender, SkillTree tree, boolean branchesExclusive)
            throws IOException {
        service().changeBranchesExclusive(tree, branchesExclusive, sender);
    }

    @Command(aliases = "preview", min = 1,
            desc = "Zeigt Vorschau",
            help = "Zeigt eine Vorschau des Skilltrees",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_TREE)
    public void showPreview(EPPlugin plugin, @Sender Player player, SkillTree tree)
            throws IOException, RenderingException {
        EditNodeMenu.openNew(plugin, player, tree);
    }

    @Command(aliases = "info", min = 1,
            desc = "Zeigt Infos zu einem Skilltree",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_BASIC)
    public void skillInfo(CommandSender sender, SkillTree tree) {
        I18n.sendLoc(sender, Format.header(Message.of("admin!tree.info.header", tree.getId())));
        I18n.sendLoc(sender, Format.result(Message.of("admin!tree.info.name", names.displayName(tree))));
        I18n.sendLoc(sender, Format.result(Message.of("admin!tree.info.desc", names.description(tree))));
        I18n.sendLoc(sender, Format.result(Message.of("admin!tree.info.be", Format.bool(tree.areBranchesExclusive()))));
        I18n.sendLoc(sender, Format.result(Message.of("admin!tree.info.misc",
                tree.getSlotId(), Format.bool(tree.getIconStack() != null))));
        ComponentSender.sendTo(
                new XyComponentBuilder(I18n.loc(sender, "admin!tree.info.preview-button"), ChatColor.GOLD).italic(true)
                        .hintedCommand("/sta preview " + tree.getId())
                        .create(),
                sender
        );
    }

    @Command(aliases = "list",
            desc = "Listet Skilltrees auf",
            help = "Listet alle Skilltrees\nin einem Inventar auf")
    @EnumRequires(Permission.ADMIN_BASIC)
    public void list(SelectTreeMenu.Factory menuFactory, @Sender Player player) {
        menuFactory.openMenu(player, tree -> skillInfo(player, tree));
    }

    @Command(aliases = "remove", min = 1,
            desc = "Löscht einen Skilltree",
            usage = "[id]")
    @EnumRequires({Permission.ADMIN_TREE, Permission.ADMIN_OVERRIDE})
    public void removeSkill(CommandSender sender, SkillTree tree)
            throws IOException {
        service().getManager().remove(tree);
    }
}
