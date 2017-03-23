/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.i18n;

import li.l1t.common.intake.i18n.Message;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SkillTree;

/**
 * Figures out display names for different Expvp entities.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public interface DisplayNameService {
    /**
     * @param skill the skill to display
     * @return a message object representing given skill's display name
     */
    Message displayName(Skill skill);

    /**
     * @param tree the tree to display
     * @return a message object representing given tree's display name
     */
    Message displayName(SkillTree tree);
}
