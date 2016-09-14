/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.service;

import li.l1t.common.intake.CommandsManager;
import li.l1t.common.intake.exception.UserException;
import li.l1t.common.inventory.SlotPosition;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.command.provider.YamlObjectProvider;
import me.minotopia.expvp.skilltree.SkillTree;
import me.minotopia.expvp.skilltree.SkillTreeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

/**
 * Provides commonly used utilities for commands working with skill trees.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-06
 */
public class SkillTreeCommandService extends YamlManagerCommandService<SkillTree> {

    public SkillTreeCommandService(SkillTreeManager manager, EPPlugin plugin) {
        super(plugin, manager, "Skilltree");
    }

    public void registerInjections(CommandsManager commandsManager) {
        commandsManager.bind(SkillTreeManager.class).toInstance(getManager());
        commandsManager.bind(SkillTreeCommandService.class).toInstance(this);
        commandsManager.bind(SkillTree.class).toProvider(new YamlObjectProvider<>(this));
    }

    public void changeIconStack(SkillTree tree, ItemStack newStack, CommandSender sender) {
        ItemStack previousStack = tree.getIconStack();
        tree.setIconStack(newStack);
        saveObject(tree);
        sendChangeNotification("Icon", previousStack, newStack, tree, sender);
    }

    public void changeSlotId(SkillTree tree, int newSlotId, CommandSender sender) {
        if (!SlotPosition.fromSlotId(newSlotId).isValidSlot()) {
            throw new UserException("Das ist keine valide Slot-ID: " + newSlotId);
        }
        int previous = tree.getSlotId();
        tree.setSlotId(newSlotId);
        saveObject(tree);
        sendChangeNotification("Iconslot", previous, newSlotId, tree, sender);
    }

    public void changeBranchesExclusive(SkillTree tree, boolean newValue, CommandSender sender) {
        boolean previous = tree.areBranchesExclusive();
        tree.setBranchesExclusive(newValue);
        saveObject(tree);
        sendChangeNotification("Branches Exclusive", previous, newValue, tree, sender);
    }

    @Override
    public SkillTreeManager getManager() {
        return (SkillTreeManager) super.getManager();
    }
}
