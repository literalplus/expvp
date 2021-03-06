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
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.command.provider.YamlObjectProvider;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skill.meta.SkillManager;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

/**
 * Provides commonly used utilities for commands working with skills.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-06
 */
public class SkillCommandService extends YamlManagerCommandService<Skill> {
    @Inject
    public SkillCommandService(SkillManager skillManager, PlayerDataService playerDataService,
                               SessionProvider sessionProvider, CommandsManager commandsManager) {
        super(sessionProvider, playerDataService, skillManager, "admin!skill.skill", commandsManager);
        registerInjections(commandsManager);
    }

    @Override
    protected void registerInjections(CommandsManager commandsManager) {
        commandsManager.bind(SkillCommandService.class).toInstance(this);
        commandsManager.bind(Skill.class).toProvider(new YamlObjectProvider<>(this));
    }

    public void changeHandlerSpec(Skill skill, String newSpec, CommandSender sender) {
        if (!newSpec.startsWith("/")) {
            throw new I18nUserException("error!skill.handlerspec-slash");
        }
        String previousName = skill.getHandlerSpec();
        skill.setHandlerSpec(newSpec);
        saveObject(skill);
        sendChangeNotification("admin!skill.prop.handler", previousName, newSpec, skill, sender);
    }

    public void changeIconStack(Skill skill, ItemStack newStack, CommandSender sender) {
        ItemStack previousStack = skill.getIconStack();
        skill.setIconStack(newStack);
        saveObject(skill);
        sendChangeNotification("admin!reg.prop.icon", previousStack, newStack, skill, sender);
    }

    public void changeBookCost(Skill skill, int newCost, CommandSender sender) {
        int previousCost = skill.getTalentPointCost();
        skill.setBookCost(newCost);
        saveObject(skill);
        sendChangeNotification(I18n.loc(sender, "admin!skill.prop.cost"), previousCost, newCost, skill, sender);
    }


    @Override
    public SkillManager getManager() {
        return (SkillManager) super.getManager();
    }
}
