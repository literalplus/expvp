/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.sk89q.intake.Command;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.ui.menu.SelectTreeMenu;
import me.minotopia.expvp.ui.menu.SkillTreeMenu;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * A command that provides tools for creation, deletion and modification of skill metadata.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class CommandSkills {
    @Command(aliases = "",
            desc = "Zeigt Skilltrees",
            help = "Zeigt ein Menü,\nin dem Skilltrees\nausgewählt und Skills\nerforscht werden können.")
    public void newSkill(EPPlugin plugin, @Sender Player player)
            throws IOException {
        SelectTreeMenu.openNew(
                plugin, player,
                tree -> SkillTreeMenu.openForResearch(plugin, player, tree)
        );
    }
}
