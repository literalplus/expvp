/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.skill;

import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.skill.meta.Skill;

import java.util.Collection;

/**
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public interface SkillService {
    /**
     * Gets the model skills obtained by given player data object.
     *
     * @param playerData the player data to retrieve skill ids from
     * @return the collection of skills given player data has obtained
     */
    Collection<Skill> getSkills(PlayerData playerData);
}
