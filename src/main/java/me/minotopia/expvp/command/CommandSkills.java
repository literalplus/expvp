/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.ui.menu.SelectTreeMenu;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * A command that provides tools for creation, deletion and modification of skill metadata.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-07-23
 */
//@AutoRegister("skills")
public class CommandSkills {
    @Inject
    public CommandSkills() {

    }

    @Command(aliases = "",
            desc = "cmd!skills.root.desc")
    public void newSkill(SelectTreeMenu.Factory menuFactory, @Sender Player player)
            throws IOException {
        menuFactory.openForResearch(player);
    }
}
