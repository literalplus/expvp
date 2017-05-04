/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command.chat;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import li.l1t.common.intake.provider.annotation.Colored;
import li.l1t.common.intake.provider.annotation.Merged;
import li.l1t.common.intake.provider.annotation.OnlineSender;
import me.minotopia.expvp.api.chat.message.PMService;
import me.minotopia.expvp.command.AutoRegister;
import org.bukkit.command.CommandSender;

/**
 * Allows players to privately message each other.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
@AutoRegister(value = "msg", aliases = {"whisper", "tell", "t", "m"})
public class CommandMessage {
    private final PMService pmService;

    @Inject
    public CommandMessage(PMService pmService) {
        this.pmService = pmService;
    }

    @Command(aliases = "", desc = "cmd!msg.root.desc", min = 2)
    public void root(CommandSender sender, @OnlineSender CommandSender receiver, @Merged @Colored String message) {
        pmService.sendMessage(sender, receiver, message);
    }
}
