/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

import li.l1t.common.intake.i18n.Message;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SkillTree;

/**
 * Resolves display names for Expvp entities.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class EPDisplayNameService implements DisplayNameService {
    @Override
    public Message displayName(Skill skill) {
        return Message.of("skill/" + skill.getId() + ".name");
    }

    @Override
    public Message displayName(SkillTree tree) {
        return Message.of("tree/" + tree.getId() + ".name");
    }
}
