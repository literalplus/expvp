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

package me.minotopia.expvp.skill.meta;

import com.google.inject.Inject;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.api.service.ResearchService;
import me.minotopia.expvp.api.skill.SkillService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.Plurals;
import me.minotopia.expvp.skilltree.SkillTreeNode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides skill instances.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public class SimpleSkillService implements SkillService {
    private final SkillManager skillManager;
    private final ResearchService researchService;
    private final DisplayNameService names;

    @Inject
    public SimpleSkillService(SkillManager skillManager, ResearchService researchService, DisplayNameService names) {
        this.skillManager = skillManager;
        this.researchService = researchService;
        this.names = names;
    }

    @Override
    public Collection<Skill> getSkills(PlayerData playerData) {
        return playerData.getSkills().stream()
                .map(ObtainedSkill::getSkillId)
                .map(skillManager::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Skill> getAllSkills() {
        return skillManager.getAll();
    }

    @Override
    public ItemStack createSkillIconFor(SkillTreeNode<?> node, Player player) {
        Skill skill = node.getValue();
        if (skill == null) {
            return new ItemStack(Material.BARRIER);
        }
        boolean obtained = researchService.has(player.getUniqueId(), skill);
        ItemStackFactory icon = createRawSkillIconFor(skill, obtained, player);
        if (obtained) {
            icon.glow().lore(I18n.loc(player, "core!research.already"));
        } else if (!doesParentPermitObtainment(node, player)) {
            icon.lore(I18n.loc(player, "core!research.notyet"));
        } else {
            icon.lore(I18n.loc(player, "core!research.possible", Plurals.talentPointPlural(skill.getTalentPointCost())));
        }
        return icon.produce();
    }

    @Override
    public ItemStackFactory createRawSkillIconFor(Skill skill, boolean obtained, CommandSender receiver) {
        if (skill == null) {
            return new ItemStackFactory(Material.BARRIER);
        }
        ItemStackFactory icon = new ItemStackFactory(skill.getDisplayStack());
        icon.displayName(findColoredDisplayNameFor(skill, obtained, receiver));
        icon.lore(I18n.loc(receiver, names.description(skill)));
        icon.amount(skill.getTalentPointCost());
        return icon;
    }

    private boolean doesParentPermitObtainment(SkillTreeNode<?> node, Player player) {
        SkillTreeNode<?> parent = node.getParent();
        return parent == null ||
                (parent.getValue() != null &&
                        researchService.has(player.getUniqueId(), parent.getValue()));
    }

    private String findColoredDisplayNameFor(Skill skill, boolean obtained, CommandSender receiver) {
        return (obtained ? "§a" : "§c") + I18n.loc(receiver, names.displayName(skill));
    }
}
