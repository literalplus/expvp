/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.chat.message;

import org.bukkit.command.CommandSender;

/**
 * Dispatches private messages.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
public interface PMService {
    void sendMessage(CommandSender sender, CommandSender receiver, String message);
}
