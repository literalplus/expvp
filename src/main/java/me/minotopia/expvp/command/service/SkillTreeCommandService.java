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

package me.minotopia.expvp.command.service;

import com.google.inject.Inject;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.inventory.SlotPosition;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.command.provider.YamlObjectProvider;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import me.minotopia.expvp.skilltree.SkillTree;
import me.minotopia.expvp.skilltree.SkillTreeManager;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

/**
 * Provides commonly used utilities for commands working with skill trees.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-06
 */
public class SkillTreeCommandService extends YamlManagerCommandService<SkillTree> {
    @Inject
    public SkillTreeCommandService(SkillTreeManager manager, SessionProvider sessionProvider,
                                   PlayerDataService playerDataService, CommandsManager commandsManager) {
        super(sessionProvider, playerDataService, manager, "admin!tree.tree", commandsManager);
    }

    @Override
    protected void registerInjections(CommandsManager commandsManager) {
        commandsManager.bind(SkillTreeCommandService.class).toInstance(this);
        commandsManager.bind(SkillTree.class).toProvider(new YamlObjectProvider<>(this));
    }

    public void changeIconStack(SkillTree tree, ItemStack newStack, CommandSender sender) {
        ItemStack previousStack = tree.getIconStack();
        tree.setIconStack(newStack);
        saveObject(tree);
        sendChangeNotification("admin!reg.prop.icon", previousStack, newStack, tree, sender);
    }

    public void changeSlotId(SkillTree tree, int newSlotId, CommandSender sender) {
        if (!SlotPosition.fromSlotId(newSlotId).isValidSlot()) {
            throw new I18nUserException("error!inv.invalid-slot", newSlotId);
        }
        int previous = tree.getSlotId();
        tree.setSlotId(newSlotId);
        saveObject(tree);
        sendChangeNotification("admin!tree.prop.slot", previous, newSlotId, tree, sender);
    }

    public void changeBranchesExclusive(SkillTree tree, boolean newValue, CommandSender sender) {
        boolean previous = tree.areBranchesExclusive();
        tree.setBranchesExclusive(newValue);
        saveObject(tree);
        sendChangeNotification("admin!tree.prop.be", previous, newValue, tree, sender);
    }

    @Override
    public SkillTreeManager getManager() {
        return (SkillTreeManager) super.getManager();
    }
}
