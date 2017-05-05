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
import me.minotopia.expvp.EPPlugin;
import me.minotopia.expvp.ui.menu.MainMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A command that allows players to open the Expvp main menu.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
@AutoRegister(value = "ep")
public class CommandEP {
    private final MainMenu.Factory menuFactory;
    private static final String[] testers = {
            "chris301234", "Krummschi", "BergFelix", "Knuddelhero", "B0mm3l6", "HamsterCrack", "El_Berndo", "_Daimn_",
            "Tw_333", "tobias1198", "Crafter_F12", "CRiiNZ", "Xc3pt1on", "Obi_Maus", "DeathlyPhantom", "ItsTomSanderson",
            "miggi66 §e(NCP Tester)", "skiller4ever"
    };

    @Inject
    public CommandEP(MainMenu.Factory menuFactory) {
        this.menuFactory = menuFactory;
    }

    //@Command(aliases = "", desc = "cmd!menu.root.desc")
    public void root(@Sender Player player) {
        menuFactory.openMenuFor(player);
    }

    @Command(aliases = "", desc = "cmd!menu.info.desc")
    public void info(EPPlugin plugin, CommandSender sender) {
        sender.sendMessage("§6Expvp Minecraft Game Mode for MinoTopia.me");
        sender.sendMessage("§6Copyright (C) 2016-" + LocalDateTime.now().getYear() + " Literallie (https://l1t.li/)");
        sender.sendMessage("§e" + plugin.getPluginVersion());
        sender.sendMessage("§6Thanks to everyone who helped test Expvp:");
        sender.sendMessage(Arrays.stream(testers)
                .sorted()
                .collect(Collectors.joining("§e, §6", "§6", "")));
    }
}
