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
import li.l1t.common.chat.ComponentSender;
import li.l1t.common.chat.XyComponentBuilder;
import li.l1t.common.intake.exception.UserException;
import li.l1t.common.intake.provider.annotation.Colored;
import li.l1t.common.intake.provider.annotation.ItemInHand;
import li.l1t.common.intake.provider.annotation.Merged;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.command.permission.EnumRequires;
import me.minotopia.expvp.command.service.SkillTreeCommandService;
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
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class CommandSkillTreeAdmin extends YamlManagerCommandBase<SkillTree, SkillTreeCommandService> {

    @Override
    public String getObjectTypeName() {
        return "Skilltree";
    }

    @Command(aliases = "new", min = 2,
            desc = "Erstellt neuen Skilltree",
            help = "Erstellt einen neuen Skilltree\nDie Id besteht " +
                    "dabei aus\nZahlen, Buchstaben und Bindestrichen\nund ist eindeutig.",
            usage = "[id] [name...]")
    @EnumRequires(Permission.ADMIN_TREE)
    public void newSkill(CommandSender sender,
                         @Validate(regex = "[a-zA-Z0-9\\-]+") String id,
                         @Merged @Colored String name)
            throws IOException {
        createNew(sender, id, name);
    }

    @Command(aliases = "name", min = 2,
            desc = "Ändert den Namen",
            usage = "[id] [name...]")
    @EnumRequires(Permission.ADMIN_TREE)
    public void editName(CommandSender sender, SkillTree tree, @Merged @Colored String name)
            throws IOException {
        if (name.length() > 32) {
            throw new UserException("Der Name darf maximal 32 Zeichen lang sein (#BlameMojang)");
        }
        service().changeName(tree, name, sender);
    }

    @Command(aliases = "icon", min = 2,
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

    @Command(aliases = "branches-exclusive", min = 2,
            desc = "§e",
            help = "Setzt, ob sich die Äste\ndes Baums gegenseitig\nausschließen.\nDas bedeutet, " +
                    "wenn true,\nund man einen Ast erforscht hat,\nkann man den Nachbarn\nnicht " +
                    "mehr erforschen. (true)",
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
    @EnumRequires(Permission.ADMIN_BASIC)
    public void showPreview(EPPlugin plugin, @Sender Player player, SkillTree tree)
            throws IOException, RenderingException {
        EditNodeMenu.openNew(plugin, player, tree);
    }

    @Command(aliases = "info", min = 1,
            desc = "Zeigt Infos zu einem Skilltree",
            usage = "[id]")
    @EnumRequires(Permission.ADMIN_BASIC)
    public void skillInfo(CommandSender sender, SkillTree tree) {
        sender.sendMessage(String.format("§e➩ §lSkilltree: §6%s §e(ID: %s§e)",
                tree.getDisplayName(), tree.getId()));
        sender.sendMessage(String.format("§e➩ §lBranches Exclusive (siehe /sta help): §6%s",
                tree.areBranchesExclusive()));
        sender.sendMessage(String.format("§e➩ §lSlot-Id in der Übersicht: §6%s",
                tree.getSlotId()));
        sender.sendMessage(String.format("§e➩ §lIcon: %s",
                tree.getIconStack() == null ? "§cnein" : tree.getIconStack()));
        ComponentSender.sendTo(
                new XyComponentBuilder("[Vorschau anzeigen]", ChatColor.GOLD).italic(true)
                        .hintedCommand("/sta preview " + tree.getId())
                        .create(),
                sender
        );
    }

    @Command(aliases = "list",
            desc = "Listet Skilltrees auf",
            help = "Listet alle Skilltrees\nin einem Inventar auf")
    @EnumRequires(Permission.ADMIN_BASIC)
    public void list(EPPlugin plugin, @Sender Player player) {
        SelectTreeMenu.openNew(plugin, player, tree -> skillInfo(player, tree));
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
