/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.obtainment;

import com.google.common.base.Preconditions;
import li.l1t.common.exception.UserException;
import me.minotopia.expvp.api.model.MutablePlayerData;
import me.minotopia.expvp.api.model.ObtainedSkill;
import me.minotopia.expvp.api.service.PlayerDataService;
import me.minotopia.expvp.api.service.SkillObtainmentService;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service that provides an interface for performing actions related to obtainment of skills.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-07
 */
public class SimpleSkillObtainmentService implements SkillObtainmentService {
    private final PlayerDataService playerDataService;

    public SimpleSkillObtainmentService(PlayerDataService playerDataService) {
        this.playerDataService = playerDataService;
    }

    @Override
    public void addObtainedSkill(UUID playerId, Skill skill) {
        Preconditions.checkNotNull(skill, "skill");
        MutablePlayerData playerData = playerDataService.findOrCreateDataMutable(playerId);
        checkIsNotObtained(playerId, skill);
        playerData.addSkill(skill);
        playerDataService.saveData(playerData);
    }

    private void checkIsNotObtained(UUID playerId, Skill skill) {
        if (hasObtainedSkill(playerId, skill)) {
            throw new UserException("Das ist bereits erforscht!");
        }
    }

    @Override
    public void removeObtainedSkill(UUID playerId, Skill skill) {
        Preconditions.checkNotNull(skill, "skill");
        MutablePlayerData playerData = playerDataService.findOrCreateDataMutable(playerId);
        checkIsObtained(playerId, skill);
        playerData.removeSkill(skill);
        playerDataService.saveData(playerData);
    }

    private void checkIsObtained(UUID playerId, Skill skill) {
        if (!hasObtainedSkill(playerId, skill)) {
            throw new UserException("Das ist noch nicht erforscht!");
        }
    }

    @Override
    public boolean hasObtainedSkill(UUID playerId, Skill skill) {
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
