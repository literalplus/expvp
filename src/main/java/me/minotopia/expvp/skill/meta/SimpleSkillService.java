/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
import me.minotopia.expvp.logging.LoggingManager;
import me.minotopia.expvp.skilltree.SkillTreeNode;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides skill instances.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public class SimpleSkillService implements SkillService {
    private final Logger LOGGER = LoggingManager.getLogger(SimpleSkillService.class);
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
        LOGGER.debug(playerData.getUniqueId() + " -> model: " + playerData.getSkills().stream().map(ObtainedSkill::getSkillId).collect(Collectors.joining(", ")));
        List<Skill> playerSkills = playerData.getSkills().stream()
                .map(ObtainedSkill::getSkillId)
                .map(skillManager::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        LOGGER.debug(playerData.getUniqueId() + " -> final: " + playerSkills.stream().map(Skill::getId).collect(Collectors.joining(", ")));
        return playerSkills;
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
