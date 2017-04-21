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
import me.minotopia.expvp.ui.menu.MainMenu;
import org.bukkit.entity.Player;

/**
 * A command that allows players to open the Expvp main menu.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
@AutoRegister(value = "menu", aliases = "ep")
public class CommandMainMenu {
    private final MainMenu.Factory menuFactory;

    @Inject
    public CommandMainMenu(MainMenu.Factory menuFactory) {
        this.menuFactory = menuFactory;
    }

    @Command(aliases = "", desc = "cmd!menu.root.desc")
    public void root(@Sender Player player) {
        menuFactory.openMenuFor(player);
    }
}
