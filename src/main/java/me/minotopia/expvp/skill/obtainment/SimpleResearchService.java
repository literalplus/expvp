/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.obtainment;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.exception.UserException;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.api.service.ResearchService;
import me.minotopia.expvp.i18n.exception.I18nInternalException;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import me.minotopia.expvp.skilltree.SkillTree;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service that provides an interface for performing actions related to obtainment of skills.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-07
 */
@Singleton
public class SimpleResearchService implements ResearchService {
    private final PlayerDataService playerDataService;
    private final DisplayNameService displayNames;

    @Inject
    public SimpleResearchService(PlayerDataService playerDataService, DisplayNameService displayNames) {
        this.playerDataService = playerDataService;
        this.displayNames = displayNames;
    }

    @Override
    public void research(UUID playerId, Skill skill, SkillTree tree) {
        Preconditions.checkNotNull(skill, "skill");
        MutablePlayerData playerData = playerDataService.findOrCreateDataMutable(playerId);
        checkIsNotObtained(playerId, skill);
        checkIsObtainable(playerId, skill, tree);
        playerData.addSkill(skill);
        playerDataService.saveData(playerData);
    }

    private void checkIsNotObtained(UUID playerId, Skill skill) {
        if (has(playerId, skill)) {
            throw new UserException("Das ist bereits erforscht!");
        }
    }

    @Override
    public void forget(UUID playerId, Skill skill) {
        Preconditions.checkNotNull(skill, "skill");
        MutablePlayerData playerData = playerDataService.findOrCreateDataMutable(playerId);
        checkIsObtained(playerId, skill);
        playerData.removeSkill(skill);
        playerDataService.saveData(playerData);
    }

    private void checkIsObtained(UUID playerId, Skill skill) {
        if (!has(playerId, skill)) {
            throw new UserException("Das ist noch nicht erforscht!");
        }
    }

    private void checkIsObtainable(UUID playerId, Skill skill, SkillTree tree) {
        SimpleSkillTreeNode next = findFirstSkillNodeInTree(skill, tree);
        while ((next = next.getParent()) != null) {
            Skill parentSkill = next.getValue();
            if (!has(playerId, parentSkill)) {
                throw new I18nUserException(
                        "error/tree.missing-parent",
                        displayNames.displayName(skill), displayNames.displayName(parentSkill)
                );
            }
        }
    }

    private SimpleSkillTreeNode findFirstSkillNodeInTree(Skill skill, SkillTree tree) {
        return tree.nodeStream()
                .filter(node -> skill.equals(node.getValue()))
                .findFirst()
                .orElseThrow(() -> new I18nInternalException("error/tree.skill-not-in-tree", skill.getDisplayName(), tree.getDisplayName()));
    }

    @Override
    public boolean has(UUID playerId, Skill skill) {
        MutablePlayerData playerData = playerDataService.findOrCreateDataMutable(playerId);
        return playerData.getSkills().stream()
                .anyMatch(skill::matches);
    }

    @Override
    public Collection<String> getObtainedSkills(UUID playerId) {
        MutablePlayerData playerData = playerDataService.findOrCreateDataMutable(playerId);
        return playerData.getSkills().stream()
                .map(ObtainedSkill::getSkillId)
                .collect(Collectors.toList());
    }
}